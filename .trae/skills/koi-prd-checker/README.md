# Koi PRD 校验器技能

校验 PRD 文档是否符合规范要求。

---

## 所属套件

本技能是 **PRD 全流程技能套件** 的一部分。该套件覆盖从需求文档生成到代码落地的完整流程。

### 套件技能列表

| 技能 | 功能 | 当前 |
|------|------|:----:|
| prd-generator | 生成 PRD | |
| prd-checker | 校验 PRD | ✅ |
| prd-pipeline | 编排 generator+checker | |
| prd-convert-to-json | PRD 转 JSON | |
| prd-story-loop | 编排故事执行 | |
| prd-story-executor | 执行故事 | |

### 完整流程

```
需求输入
  ↓
prd-pipeline（编排层）
  ├── prd-generator → 生成/修正 PRD
  └── prd-checker → 校验  ← 当前技能
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

本技能负责检查 PRD 的完整性和质量。

**输入：** prd-generator 或 prd-pipeline 生成的 PRD

---

## 使用方式

### 触发词

- "校验PRD"
- "检查PRD"
- "PRD规范性"

---

## 与其他技能配合

```
prd-generator → 生成 PRD
  ↓
prd-checker → 校验 PRD（可选）
  ↓
prd-convert-to-json → 转换为 prd.json
  ↓
prd-story-loop → 编排故事执行
```



---

## 文件

- `SKILL.md` - 技能定义
