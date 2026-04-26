# Koi PRD Story Executor（Koi PRD 故事执行器）

按照 PRD 中定义的用户故事顺序，逐个实施代码并验证可用性。

---

## 所属套件

本技能是 **PRD 全流程技能套件** 的一部分。该套件覆盖从需求文档生成到代码落地的完整流程。

### 套件技能列表

| 技能 | 功能 | 当前 |
|------|------|:----:|
| koi-prd-generator | 生成 PRD | |
| koi-prd-checker | 校验 PRD | |
| koi-prd-pipeline | 编排 generator+checker | |
| koi-prd-convert-to-json | PRD 转 JSON | |
| koi-prd-story-loop | 编排故事执行 | |
| koi-prd-story-executor | 执行故事 | ✅ |

### 完整流程

```
需求输入
  ↓
koi-prd-pipeline（编排层）
  ├── koi-prd-generator → 生成/修正 PRD
  └── koi-prd-checker → 校验
  ↓
koi-prd-convert-to-json → 转换为 prd-[功能名称].json
  ↓
koi-prd-story-loop → 编排故事执行
  └── koi-prd-story-executor → 执行故事  ← 当前技能
  ↓
代码产出物
```

---

## 定位

本技能是**执行层技能**，负责：
1. 读取 prd.json 文件
2. 按依赖顺序确定执行计划
3. 逐个实施故事并验证
4. 更新故事状态

---

## 前置条件

1. 已通过 `prd-convert-to-json` 将 PRD 转换为 prd.json
2. 存在 `prd-context/prd-files/prd.json` 文件
3. 项目已初始化 git 仓库

---

## 读取策略（宽容模式）

prd-story-executor 采用**宽容读取**策略：防止致命错误，允许小问题。

### 致命错误（必须报错）

| 问题 | 处理 |
|------|------|
| YAML 语法错误 | ❌ 报错 |
| stories 数组为空 | ❌ 报错 |

### 非致命问题（自动处理）

| 问题 | 处理 |
|------|------|
| 缺少可选字段 | ✅ 使用默认值 |
| 枚举值无效 | ✅ 自动矫正 |
| 依赖引用不存在 | ⚠️ 警告但不阻止 |

---

## 工作流程

```
初始化
  ↓
  读取故事
  ↓
  选择可执行故事（依赖满足 + pending）
  ↓
  实施代码
  ↓
  验证（构建 → 风格 → 测试 → UI）
  ↓
  更新状态
  ↓
  还有可执行故事？
  ├── 是 → 回到"选择可执行故事"
  └── 否 → 输出报告
```

---

## 验证机制（技术栈无关）

| 验证层 | 必须？ | 说明 |
|--------|--------|------|
| P0 - 构建 | ✅ 必须 | 必须通过 |
| P1 - 风格 | ❌ 可选 | 如配置 |
| P2 - 测试 | ❌ 可选 | 如配置 |
| P3 - UI | ⚠️ 条件 | 仅 UI 故事 |

### 命令发现顺序

```
1. _meta.yaml 中的 validation
2. 项目配置文件（package.json, Makefile 等）
3. 技术栈约定默认值
```

---

## 状态管理

| 状态 | 说明 | 可执行？ |
|------|------|---------|
| pending | 未开始 | ✅ 依赖满足时 |
| in_progress | 实施中 | ❌ |
| done | 已完成 | ❌ |
| blocked | 被阻塞 | ❌ |
| failed | 失败 | ❌ |

---

## 与其他技能配合

```
prd-generator / prd-pipeline → 生成 PRD
        ↓
prd-splitter → 拆分故事
        ↓
prd-story-executor → 逐个实施
```

---

## 触发词

- "执行故事"
- "实施这个 PRD"
- "开始开发"
- "帮我按顺序实施这些故事"


