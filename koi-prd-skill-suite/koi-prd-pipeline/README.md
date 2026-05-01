# Koi PRD 流水线技能

编排 `prd-generator` 和 `prd-checker` 实现 PRD 质量的持续改进。

---

## 所属套件

本技能是 **PRD 全流程技能套件** 的一部分。该套件覆盖从需求文档生成到代码落地的完整流程。

### 套件技能列表

| 技能 | 功能 | 当前 |
|------|------|:----:|
| prd-generator | 生成 PRD | |
| prd-checker | 校验 PRD | |
| prd-pipeline | 编排 generator+checker | ✅ |
| prd-convert-to-json | PRD 转 JSON | |
| prd-story-loop | 编排故事执行 | |
| prd-story-executor | 执行故事 | |

### 完整流程

```
需求输入
  ↓
prd-pipeline（编排层）  ← 当前技能
  ├── prd-generator → 生成/修正 PRD
  └── prd-checker → 校验
  ↓
prd-convert-to-json → 转换为 prd.json
  ↓
prd-story-loop → 编排故事执行
  └── prd-story-executor → 执行故事
  ↓
代码产出物
```

---

## 定位

本技能是**编排层（Orchestrator）**，负责调度 `prd-generator` 和 `prd-checker` 两个执行层技能：

```
prd-pipeline（编排）
    ├── prd-generator（生成/修正 PRD）
    └── prd-checker（校验 PRD）
```

---

## 使用方式

### 触发词

- "创建PRD"
- "开发新功能"
- "帮我创建一个需求文档"

---

## 工作流程

```
用户输入
  ↓
  循环（最多 3 轮）
  ├── ① 调用 prd-generator
  │   ├── 第 1 轮：生成初始 PRD
  │   └── 第 2/3 轮：根据 checker 反馈修正
  ├── ② 调用 prd-checker
  │   ├── 校验 PRD 规范性
  │   └── 输出校验报告
  └── ③ 判断是否终止
      ├── 通过 → 终止，输出最终结果 ✅
      └── 未通过 → 继续循环或达到上限 ⚠️
  ↓
输出最终 PRD + 校验报告
```

---

## 与其他技能配合

```
prd-pipeline（编排）
    ├── prd-generator（被调度）
    └── prd-checker（被调度）
            ↓
        prd-convert-to-json（后续处理）
            ↓
        prd-story-loop
            ↓
        prd-story-executor
```



---

## 文件

- `SKILL.md` - 技能定义（详细工作流和规则）
