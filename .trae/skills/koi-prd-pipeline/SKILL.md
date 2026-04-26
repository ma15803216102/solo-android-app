---
name: koi-prd-pipeline
description: "PRD 生成与校验流水线。自动执行 生成→校验→修正→复检 的循环流程，输出符合规范的 PRD。触发词：生成prd流水线、prd pipeline、创建高质量prd。"
user-invocable: true
---

# Koi PRD Pipeline（PRD 生成与校验流水线）

编排 `koi-prd-generator` 和 `koi-prd-checker` 两个技能，通过循环执行实现 PRD 质量的持续改进，直到输出符合规范的最终 PRD。

---

## 定位

本技能是**编排层（Orchestrator）**，不直接生成或检查 PRD，而是通过 **Task 工具启动独立子代理**来调度以下两个执行层技能：

- **koi-prd-generator**：负责生成和修正 PRD
- **koi-prd-checker**：负责校验 PRD 规范性

---

## 技术栈询问

在执行 pipeline 之前，首先询问用户技术栈选择：

### 应用类型选择

1. 请选择应用类型：
   - Web 应用
   - 移动应用
   - 桌面应用
   - 嵌入式应用
   - 其他类型的应用

### 技术栈询问（根据应用类型）

#### Web 应用

1. 请选择后端技术栈：
   - Java (Spring Boot)
   - Python (Django/Flask)
   - Node.js (Express/NestJS)
   - Go (Gin/Echo)
   - PHP (Laravel/Symfony)
   - Ruby (Rails)
   - 其他

2. 请选择前端技术栈：
   - Vue (Vue 3 + Pinia + Vue Router)
   - React (React 18 + Redux + React Router)
   - Angular (Angular 17 + NgRx)
   - Svelte (Svelte + SvelteKit)
   - SolidJS
   - 其他

3. 请选择数据库：
   - MySQL
   - PostgreSQL
   - MongoDB
   - Redis
   - PostgreSQL
   - 其他

4. 请选择缓存：
   - Redis
   - Memcached
   - Caffeine
   - 其他
   - 不使用

5. 请选择消息队列：
   - Kafka
   - RabbitMQ
   - RocketMQ
   - 其他
   - 不使用

6. 请选择搜索引擎：
   - Elasticsearch
   - Solr
   - 其他
   - 不使用

7. 请选择构建工具：
   - Maven
   - Gradle
   - npm
   - yarn
   - pnpm
   - 其他

8. 请选择部署方式：
   - Docker
   - Kubernetes
   - 云服务 (AWS/Azure/阿里云)
   - 传统部署
   - 其他

#### 移动应用

1. 请选择移动平台：
   - iOS
   - Android
   - 跨平台
   - 其他

2. 请选择开发框架：
   - Flutter
   - React Native
   - SwiftUI
   - Jetpack Compose
   - 其他

3. 请选择状态管理：
   - Provider
   - Riverpod
   - Redux
   - MobX
   - 其他
   - 不使用

4. 请选择网络请求：
   - Dio
   - Retrofit
   - Axios
   - 其他
   - 不使用

5. 请选择本地存储：
   - SharedPreferences
   - SQLite
   - Realm
   - 其他
   - 不使用

6. 请选择构建工具：
   - Xcode
   - Android Studio
   - Flutter CLI
   - 其他

7. 请选择部署方式：
   - App Store
   - Google Play
   - 企业部署
   - 其他

#### 桌面应用

1. 请选择桌面平台：
   - Windows
   - macOS
   - Linux
   - 跨平台
   - 其他

2. 请选择开发框架：
   - Electron
   - Qt
   - WPF
   - Cocoa
   - 其他

3. 请选择状态管理：
   - Redux
   - MobX
   - Provider
   - 其他
   - 不使用

4. 请选择本地存储：
   - SQLite
   - Realm
   - 本地文件
   - 其他
   - 不使用

5. 请选择构建工具：
   - Visual Studio
   - Xcode
   - CMake
   - 其他

6. 请选择部署方式：
   - 安装包
   - 绿色版
   - 应用商店
   - 其他

#### 嵌入式应用

1. 请选择硬件平台：
   - ARM
   - x86
   - RISC-V
   - 其他

2. 请选择操作系统：
   - Linux
   - RTOS
   - 裸机
   - 其他

3. 请选择开发语言：
   - C
   - C++
   - Python
   - 其他

4. 请选择通信协议：
   - HTTP
   - MQTT
   - CoAP
   - 其他
   - 不使用

5. 请选择构建工具：
   - GCC
   - Clang
   - CMake
   - 其他

6. 请选择部署方式：
   - 烧录
   - 远程升级
   - 其他

#### 其他类型的应用

请描述应用类型和技术栈：

## 工作流程

```
用户输入（用户故事/功能描述）
  ↓
技术栈询问
  ↓
  循环（最多 3 轮）
  ├── ① 启动 generator 子代理
  │   ├── 第 1 轮：生成初始 PRD
  │   └── 第 2/3 轮：根据 checker 反馈修正 PRD
  ├── ② 启动 checker 子代理
  │   ├── 按 11 项标准逐项校验
  │   └── 输出校验报告（通过/不通过 + 不符合项详情）
  └── ③ 判断是否终止
      ├── ✅ checker 返回"通过" → 跳出循环，输出最终结果
      ├── ❌ checker 返回"不通过" 且未达 3 轮 → 继续循环
      └── ❌ checker 返回"不通过" 且已达 3 轮 → 跳出循环
  ↓
输出最终 PRD + 校验报告
```

---

## 执行规则

### 规则 1：循环次数上限

| 轮次 | 阶段 | 说明 |
|------|------|------|
| 第 1 轮 | generator → checker | 生成初稿，首次校验 |
| 第 2 轮 | generator → checker | 针对不符合项修正，复检 |
| 第 3 轮 | generator → checker | 修复残余问题，终检 |

**最多 3 轮，不可超过。** 3 轮后无论结果如何都必须终止。

### 规则 2：上下文传递

每轮循环中，checker 的输出必须作为下一轮 generator 的输入：

```
checker 输出（不符合项详情）  →  generator 输入（修正依据）
```

**具体传递方式：**
- 将 checker 校验报告中的"不符合项详情"部分（问题描述、当前内容、改进建议）整理为修正指令
- 将修正指令传递给 generator，要求其针对这些具体问题修正 PRD
- generator 必须基于**现有 PRD 文件**进行修正，而非重新生成

### 规则 3：终止条件

按优先级排列：

1. **checker 返回"通过"** → 立即终止，输出最终结果 ✅
2. **达到 3 轮上限** → 终止，输出当前 PRD + 最后一轮校验报告 ⚠️
3. **用户主动终止** → 保留当前最新版本的 PRD

### 规则 4：质量标准

| 问题级别 | 定义 | 处理方式 |
|----------|------|----------|
| P0（必须修复） | 影响实施的关键问题（如缺少章节、用户故事颗粒度严重过大） | 必须在循环内修复 |
| P1（强烈建议） | 影响效率的问题（如技术栈模糊、目标不可衡量） | 必须在循环内修复 |
| P2（可选优化） | 最佳实践建议（如小版本号缺失、措辞优化） | 允许保留，不影响最终通过判定 |

**最终判定标准：** PRD 无 P0 和 P1 问题即可视为通过。存在 P2 问题不影响输出。

### 规则 5：子代理调用方式（核心变更）

**⚠️ 重要：本技能的所有生成和校验工作必须通过 Task 工具启动独立子代理完成，编排者（主对话）不得自己直接生成或校验 PRD。**

#### 调用 koi-prd-generator 子代理

使用 Task 工具，参数如下：

- `subagent_type`: "general_purpose_task"
- `description`: "生成/修正PRD"（第1轮）或 "修正PRD"（第2/3轮）
- `response_language`: 与用户输入语言一致
- `query`: 按以下模板填写

**第1轮（生成）query 模板：**
```
先调用 Skill('koi-prd-generator') 加载生成器指令，然后基于以下输入生成PRD并保存到 prd-context/prd-files/ 目录。用户故事：{用户故事内容}。补充说明：{补充说明内容}。如果需要澄清问题，直接根据补充说明中的信息做出合理假设，不要中断等待用户回复。
```

**第2/3轮（修正）query 模板：**
```
先调用 Skill('koi-prd-generator') 加载生成器指令，然后修正已有PRD文件 {PRD文件路径}。以下是校验反馈的不符合项，请逐一修正：{checker不符合项详情}。修正后保存到原文件路径。
```

#### 调用 koi-prd-checker 子代理

使用 Task 工具，参数如下：

- `subagent_type`: "general_purpose_task"
- `description`: "校验PRD规范"
- `response_language`: 与用户输入语言一致
- `query`: 按以下模板填写

**query 模板：**
```
先调用 Skill('koi-prd-checker') 加载校验器指令，然后校验PRD文件 {PRD文件路径} 的规范性。按11项标准逐项检查，输出完整的校验报告（包含检查结果汇总表、不符合项详情、总结和改进优先级）。最终必须在报告末尾明确给出"校验结果：✅ 通过"或"校验结果：❌ 不通过"的结论。
```

#### 上下文传递机制

子代理是独立进程，看不到主对话历史。因此：

1. **generator 子代理**需要的所有信息（用户故事、补充说明、已有PRD路径、校验反馈）必须显式写入 query 参数
2. **checker 子代理**需要的 PRD 文件路径必须显式写入 query 参数
3. 编排者从 checker 子代理的返回结果中提取校验结论（通过/不通过）和不符合项详情
4. 编排者将不符合项详情整理后，作为下一轮 generator 子代理的 query 参数传入

#### 禁止事项

- ❌ 编排者（主对话）不得自己直接编写或修改 PRD 文件
- ❌ 编排者（主对话）不得自己直接校验 PRD
- ❌ 不得跳过子代理直接使用 Write/Edit 工具操作 PRD 文件
- ✅ 所有 PRD 的生成、修正、校验必须由独立子代理完成
- ✅ 编排者只负责：循环控制、上下文传递、结果汇总

---

## 输入

本技能接收以下输入：

1. **用户故事**（必需）：格式为 `[as a]...[i want]...[so that]...`
2. **补充说明**（可选）：用户对功能的具体要求、约束条件等
3. **已有 PRD 文件路径**（可选）：如果已有 PRD 需要修正，提供文件路径

---

## 输出

### 最终输出包含：

1. **PRD 文件**：保存到 `prd-context/prd-files/prd-[功能名称].md`
2. **最终校验报告**：最后一轮 checker 的完整校验报告
3. **执行摘要**：包含以下信息
   - 总循环轮数
   - 每轮发现和修复的问题
   - 最终状态（通过 / 存在残留问题）

### 执行摘要格式：

```markdown
## Pipeline 执行摘要

- **总循环轮数：** X / 3
- **最终状态：** ✅ 通过 / ⚠️ 存在残留问题

### 各轮次记录

| 轮次 | generator 操作 | checker 结果 | 修复的问题 |
|------|---------------|-------------|-----------|
| 1 | 生成初始 PRD | 发现 X 项不符合 | — |
| 2 | 修正 PRD | 发现 X 项不符合 | 修复了 XXX |
| 3 | 修正 PRD | ✅ 通过 | 修复了 XXX |

### 残留问题（如有）

- [P2] XXX（可选优化，不影响使用）
```

---

## 异常处理

| 异常场景 | 处理方式 |
|----------|----------|
| generator 生成的 PRD 格式严重错误（无法被 checker 解析） | 终止循环，提示用户检查输入是否清晰 |
| checker 连续 3 轮报告相同问题（修正无效） | 终止循环，输出当前 PRD + 校验报告，提示用户手动介入 |
| 用户在循环过程中提供新需求 | 将新需求追加到 generator 的输入中，继续当前循环 |
| prd-context/ 目录创建失败 | 提示用户手动创建目录后重试 |
| 子代理执行超时或失败 | 重试一次，仍失败则终止循环并提示用户 |

---

## 完整执行示例

```
用户：帮我为预审记录管理系统生成 PRD，用户故事是 [as a]预审记录操作服务 [i want]对预审记录进行新增、查询、编辑、删除 [so that]实现预审数据规范化管理

Pipeline 执行：

【第 1 轮】
  → 启动 generator 子代理：生成初始 PRD（22 个用户故事）
  → 启动 checker 子代理：发现 3 项不符合（颗粒度、技术栈、目标量化）
  → 结果：❌ 不通过，继续

【第 2 轮】
  → 启动 generator 子代理：传入 checker 的 3 项不符合详情，修正 PRD
  → 启动 checker 子代理：发现 0 项不符合
  → 结果：✅ 通过，终止

输出：
  - PRD 文件：prd-context/prd-files/prd-pre-review-management.md
  - 校验报告：11/11 项通过
  - 执行摘要：2 轮完成，所有问题已修复
```

---

## 与其他技能的关系

```
prd-pipeline（本技能，编排层 — 由主对话执行）
  ├── Task(general_purpose_task) → 执行 koi-prd-generator（独立子代理）
  └── Task(general_purpose_task) → 执行 koi-prd-checker（独立子代理）
```

本技能不替代 koi-prd-generator 或 koi-prd-checker，用户仍可单独调用它们。
本技能的价值在于**通过独立子代理实现真正的生成-校验分离**，确保 PRD 输出质量。
