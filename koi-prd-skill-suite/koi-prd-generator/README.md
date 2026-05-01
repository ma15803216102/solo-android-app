# Koi PRD 生成器技能

生成符合规范的产品需求文档（PRD）。

---

## 所属套件

本技能是 **PRD 全流程技能套件** 的一部分。该套件覆盖从需求文档生成到代码落地的完整流程。

### 套件技能列表

| 技能 | 功能 | 当前 |
|------|------|:----:|
| prd-generator | 生成 PRD | ✅ |
| prd-checker | 校验 PRD | |
| prd-pipeline | 编排 generator+checker | |
| prd-convert-to-json | PRD 转 JSON | |
| prd-story-loop | 编排故事执行 | |
| prd-story-executor | 执行故事 | |

### 完整流程

```
需求输入
  ↓
prd-pipeline（编排层）
  ├── prd-generator → 生成/修正 PRD  ← 当前技能
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

本技能负责根据用户需求生成 PRD 文档。

**输出：** `prd-context/prd-files/prd-[名称].md`

---

## 使用方式

### 触发词

- "生成PRD"
- "创建PRD"
- "写一个PRD"
- "需求文档"

### 工作流程

```
澄清问题 → 生成 PRD → 保存文件
```

---

## 核心特性

- ✅ 带字母选项的澄清问题，高效沟通
- ✅ 小而具体的用户故事
- ✅ 可验证的验收标准
- ✅ 自动提取技术栈信息

---

## 与其他技能配合

```
prd-generator → prd-pipeline（可选）
  ↓
  生成 PRD
  ↓
  ├── prd-checker（可选）
  └── prd-convert-to-json
        ↓
      prd-story-loop
        ↓
      prd-story-executor
```



---

## 文件

- `SKILL.md` - 技能定义
- `README.md` - 本文档
