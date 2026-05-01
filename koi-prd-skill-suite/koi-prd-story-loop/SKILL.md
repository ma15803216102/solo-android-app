---
name: koi-prd-story-loop
description: "故事循环执行器。读取prd-[功能名称].json，按依赖顺序循环调度子代理逐个执行用户故事，直到全部完成。触发词：执行故事循环、开始执行故事、自动执行故事。"
user-invocable: true
---

# Koi 故事循环执行器技能

读取 `prd-[功能名称].json`，循环调度子代理逐个执行用户故事，直到全部完成或达到最大迭代次数。

---

## 定位

本技能是**编排层技能**，由主对话执行。不直接编写代码，而是通过 Task 工具启动独立子代理来执行每个故事。

```
prd-story-loop（本技能，编排层 — 主对话执行）
  └── Task(general_purpose_task) → 执行 koi-prd-story-executor（独立子代理）
```

---

## 前置条件

1. `prd-[功能名称].json` 文件已存在（通过 `koi-prd-convert-to-json` 技能生成，位于 `prd-context/prd-files/` 目录）
2. 项目目录已初始化（git 已初始化）
3. `development-log.txt` 文件已初始化（位于 `prd-context/execution-logs/` 目录，如不存在则自动创建）

## 执行规则

### 规则 1：代办生成要求（可选）
- 如果智能体平台支持代办（plan）功能，执行时必须为每个用户故事生成对应的代办
- 代办数量必须大于或等于用户故事的数量
- 除了用户故事对应的代办外，可以根据需要添加额外的计划任务
- 代办应包含故事 ID、标题和简要描述

### 规则 2：代办跟踪要求（可选）
- 如果使用了代办功能，每个代办必须明确关联到对应的用户故事
- 代办状态必须与用户故事的执行状态保持同步
- 执行过程中必须更新代办状态（待办/进行中/完成/失败）
- 执行完成后必须生成代办跟踪报告

---

## 工作流程

```
初始化
  ↓
① 读取 prd-context/prd-files/prd-[功能名称].json，统计故事总数和待执行数量
  ↓
② 生成代办计划（可选）
     - 如果智能体平台支持代办功能，为每个用户故事生成对应的代办
     - 添加必要的额外计划任务
     - 记录代办到 development-log.txt
  ↓
③ 检查/初始化 prd-context/execution-logs/development-log.txt
  ↓
④ 检查/初始化 git 仓库
  ⑤ 检查/初始化 prd-context/conventions/agents.md（如果不存在则创建空文件）
  ↓
循环（最多 maxIterations 轮）
  ↓
  ① 从 prd-[功能名称].json 找到下一个可执行故事
     - passes: false
     - 所有依赖（dependencies）的 passes 均为 true
     - priority 最小
  ↓
  ② 更新代办状态为"进行中"（可选）
     - 如果使用了代办功能，更新对应代办的状态
  ↓
  ② 启动子代理（Task 工具）
     - 传入：当前故事信息 + prd-context/execution-logs/development-log.txt 上下文
     - 子代理执行：读取上下文 → 写代码 → 验证 → 提交 → 更新文件
  ↓
  ③ 检查子代理返回结果
     - 成功 → 继续下一个故事
     - 失败 → 记录原因，跳过或重试
  ↓
  ④ 检查终止条件
     - 全部 passes: true → 输出完成报告，终止
     - 达到最大迭代次数 → 输出进度报告，终止


---

## 子代理调用方式

使用 Task 工具启动子代理，参数如下：

- `subagent_type`: "general_purpose_task"
- `description`: "执行故事 [Story ID]"
- `response_language`: 与用户输入语言一致
- `query`: 按以下模板填写

### query 模板

```
先调用 Skill('koi-prd-story-executor') 加载执行器指令，然后执行以下故事：

## 当前任务
- 故事 ID：{storyId}
- 故事标题：{storyTitle}
- 故事描述：{storyDescription}
- 验收标准：
{acceptanceCriteria，每项一行}
- 故事类型：{storyType}
- 依赖：{dependencies}

## 项目信息
- 项目根目录：{projectRoot}
- prd-[功能名称].json 路径：prd-context/prd-files/prd-[功能名称].json
- development-log.txt 路径：prd-context/execution-logs/development-log.txt
- 技术栈：{techStack}

## 执行要求
1. 先读取 prd-context/prd-files/prd-[功能名称].json 和 prd-context/execution-logs/development-log.txt 获取上下文
2. 读取 git 历史了解已有代码
3. 只做这一个故事，保持变更最小化
4. 验证通过后 git commit
5. 更新 prd-context/prd-files/prd-[功能名称].json（passes: true）
6. 追加 prd-context/execution-logs/development-log.txt
7. 如果有新发现，更新 prd-context/conventions/agents.md
8. 完成后报告结果（成功/失败 + 变更文件列表 + 经验总结）
```

---

## 上下文传递机制

子代理是独立进程，看不到主对话历史。因此：

1. **子代理需要的所有信息**必须显式写入 query 参数
2. **子代理自己读取文件**获取详细上下文（prd-[功能名称].json、development-log.txt、git log、prd-context/conventions/agents.md）
3. **编排者从子代理返回结果中提取**执行状态
4. **编排者不需要传递代码内容**，子代理会自己读 git 和文件系统

---

## 并行执行策略

对于**无依赖关系**的故事，可以并行启动多个子代理：

```
US-001（无依赖）──┐
US-002（无依赖）──┤── 可并行
US-003（无依赖）──┘
US-004（依赖 US-001）── 必须等 US-001 完成
```

### 并行规则

1. 扫描所有 `passes: false` 的故事
2. 找出依赖全部满足的故事集合
3. 如果集合中有多个故事，可并行启动（最多 3 个并行子代理）
4. 并行故事完成后，重新扫描，继续下一批

### 串行 vs 并行决策

| 场景 | 策略 | 原因 |
|------|------|------|
| scaffold 故事（项目初始化、数据库设计） | 串行 | 后续所有故事都依赖它们 |
| 同类型后端接口故事 | 可并行 | 互不依赖 |
| 前端页面 + 对应后端接口 | 串行 | 前端依赖后端接口 |
| fullstack 故事 | 串行 | 涉及前后端联合变更 |

---

## 终止条件

按优先级排列：

1. **全部故事 passes: true** → 立即终止，输出完成报告
2. **达到最大迭代次数** → 终止，输出进度报告
3. **连续 3 个故事失败** → 终止，输出错误报告
4. **用户主动终止** → 保留当前进度，可随时恢复

---

## 恢复机制

如果执行中断（达到最大迭代次数或用户终止），可以随时恢复：

1. `prd-[功能名称].json` 中记录了每个故事的 `passes` 状态
2. `progress.txt` 中记录了每次迭代的详细日志
3. 重新调用本技能时，会自动跳过已完成的 story

---

## 输出报告

### 完成报告（全部通过）

```
## 执行完成

- 总故事数：66
- 已完成：66
- 失败：0
- 总迭代次数：68（含 2 次重试）

### 执行时间线
| 迭代 | 故事 | 类型 | 状态 | 说明 |
|------|------|------|------|------|
| 1 | US-001 | scaffold | 通过 | 后端项目初始化 |
| 2 | US-002 | scaffold | 通过 | 核心业务表设计 |
| ... | ... | ... | ... | ... |
```

### 进度报告（未全部完成）

```
## 执行进度

- 总故事数：66
- 已完成：42
- 待执行：20
- 失败：4
- 总迭代次数：50

### 失败故事
| 故事 | 失败原因 | 建议 |
|------|---------|------|
| US-025 | mvn compile 失败 | 检查依赖冲突 |
| ... | ... | ... |
```

---

## 异常处理

| 异常场景 | 处理方式 |
|---------|---------|
| 子代理执行超时 | 终止子代理，标记故事为失败，继续下一个 |
| 子代理返回错误 | 记录错误信息，跳过该故事 |
| git 冲突 | 标记故事为失败，提示用户手动解决 |
| prd-[功能名称].json 损坏 | 终止循环，提示用户检查文件 |

---

## 与其他技能的配合

```
koi-prd-generator / koi-prd-pipeline → 生成 PRD（Markdown）
  ↓
koi-prd-convert-to-json → 转换为 prd.json
  ↓
koi-prd-story-loop → 循环调度执行（本技能）
  ↓
koi-prd-story-executor → 子代理执行单个故事
```
