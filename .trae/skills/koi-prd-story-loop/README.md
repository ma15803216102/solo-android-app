# Koi 故事循环执行器技能

编排子代理逐个执行用户故事，直到全部完成。

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
| koi-prd-story-loop | 编排故事执行 | ✅ |
| koi-prd-story-executor | 执行故事 | |

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
koi-prd-story-loop → 编排故事执行 ← 当前技能
  └── koi-prd-story-executor → 执行故事
  ↓
代码产出物
```

---

## 定位

本技能是**编排层技能**，由主对话执行。不直接编写代码，而是通过 Task 工具启动独立子代理来执行每个故事。

```
prd-story-loop（本技能，编排层 — 主对话执行）
  └── Task(general_purpose_task) → 执行 koi-prd-story-executor（独立子代理）
```

---

## 使用方式

### 触发词

- "执行故事循环"
- "开始执行故事"
- "自动执行故事"

---

## 工作流程

```
初始化
  ↓
① 读取 prd-context/prd-files/prd-[功能名称].json，统计故事总数和待执行数量
  ↓
② 检查/初始化 prd-context/execution-logs/development-log.txt
  ↓
③ 检查/初始化 git 仓库
  ↓
循环（最多 maxIterations 轮）
  ↓
  ① 从 prd-[功能名称].json 找到下一个可执行故事
     - passes: false
     - 所有依赖（dependencies）的 passes 均为 true
     - priority 最小
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
```

---

## 与其他技能配合

```
koi-prd-convert-to-json → 转换为 prd-[功能名称].json
  ↓
koi-prd-story-loop → 编排故事执行 ← 当前技能
  └── koi-prd-story-executor → 执行故事
```



---

## 文件

- `SKILL.md` - 技能定义
