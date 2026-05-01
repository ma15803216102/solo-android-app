---
name: koi-prd-convert-to-json
description: "将 PRD 文档转换为 prd.json 格式，用于故事循环执行器逐故事执行。触发词：转换 prd、转 json、create prd.json、prd 转 json。"
user-invocable: true
---

# Koi PRD 转 JSON 技能

将已有的 PRD（Markdown 格式）转换为 `prd.json` 格式，供故事循环执行器逐个用户故事执行。

---

## 工作内容

1. 读取已有的 PRD Markdown 文件（从 `prd-context/prd-files/prd-[功能名称].md`）
2. 解析其中的用户故事、验收标准、依赖关系等信息
3. 按照标准 prd.json 格式规范进行转换
4. 保存为 `prd-context/prd-files/prd-[功能名称].json` 文件

**重要：**
- 不要开始实施代码，只做格式转换。
- 转换前先检查是否已存在 prd.json，如果存在且 `branchName` 不同，先归档旧文件。
- 转换完成后输出转换摘要（故事总数、类型分布等）。

---

## 输出格式

```json
{
  "project": "[项目名称]",
  "branchName": "prd/[feature-name-kebab-case]",
  "description": "[功能描述，取自PRD标题/概述]",
  "techStack": {
    "appType": "[应用类型]",
    "frontend": "[前端技术栈]",
    "backend": "[后端技术栈]",
    "database": "[数据库]",
    "buildTool": "[构建工具]",
    "stateManagement": "[状态管理]",
    "other": "[其他技术栈]"
  },
  "userStories": [
    {
      "id": "US-001",
      "title": "[故事标题]",
      "description": "As a [用户角色], I want [功能] so that [收益]",
      "acceptanceCriteria": [
        "验收标准1",
        "验收标准2",
        "代码通过编译/类型检查/lint检查无错误"
      ],
      "priority": 1,
      "dependencies": [],
      "type": "scaffold",
      "passes": false,
      "notes": ""
    }
  ]
}
```

---

## 字段说明

### 顶层字段

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `project` | string | 是 | 项目名称，取自 PRD 标题 |
| `branchName` | string | 是 | Git 分支名，格式 `prd/[feature-kebab-case]` |
| `description` | string | 是 | 功能描述，取自 PRD 概述的第一段 |
| `techStack` | object | 否 | 技术栈信息，从 PRD 的技术栈章节提取 |
| `techStack.appType` | string | 否 | 应用类型（Web/移动/桌面/嵌入式/其他） |
| `techStack.frontend` | string | 否 | 前端技术栈 |
| `techStack.backend` | string | 否 | 后端技术栈 |
| `techStack.database` | string | 否 | 数据库 |
| `techStack.buildTool` | string | 否 | 构建工具 |
| `techStack.stateManagement` | string | 否 | 状态管理 |
| `techStack.other` | string | 否 | 其他技术栈信息 |
| `userStories` | array | 是 | 用户故事数组 |

### userStories 数组元素

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | string | 是 | 故事编号，格式 US-XXX（三位数字） |
| `title` | string | 是 | 故事标题 |
| `description` | string | 是 | 用户故事描述 |
| `acceptanceCriteria` | string[] | 是 | 验收标准数组 |
| `priority` | number | 是 | 优先级数字（1, 2, 3...） |
| `dependencies` | string[] | 是 | 依赖的故事 ID 数组 |
| `type` | string | 是 | scaffold / backend / frontend / fullstack |
| `passes` | boolean | 是 | 初始值 false |
| `notes` | string | 是 | 初始值空字符串 |

### type 字段取值规则

| 类型 | 适用场景 |
|------|---------|
| `scaffold` | 项目初始化、数据库表设计、框架搭建 |
| `backend` | 后端接口实现、服务层逻辑 |
| `frontend` | 前端页面、UI 组件 |
| `fullstack` | 同时涉及前后端变更的故事 |

---

## 故事大小规则

每个故事必须能在一次 AI 会话内完成。不能用 2-3 句话描述完的变更，说明故事太大需要拆分。

---

## 故事排序

按依赖顺序：scaffold → backend → frontend → fullstack。前面的故事不能依赖后面的故事。

---

## 验收标准规则

- 所有故事必须包含"代码通过编译/类型检查/lint检查无错误"
- 前端故事还应包含"在浏览器中验证页面功能和样式"
- 每条标准必须可验证，不能模糊

---

## 转换规则

1. PRD 中的 US-XXX 编号直接保留，priority 按编号顺序
2. 描述从 PRD 提取，移除（技术故事）标注，格式化为 As a [角色], I want [功能] so that [收益]
3. 验收标准从 - [ ] 列表提取，移除复选框标记
4. 依赖从 PRD 提取，解析为字符串数组
5. 类型根据标题/描述推断
6. branchName 保持 prd/ 前缀不变
7. 技术栈信息从 PRD 的技术栈章节提取，包括应用类型、前端、后端、数据库、构建工具、状态管理等

---

## 归档机制

写入新 prd-[功能名称].json 前，检查已有同名文件的 branchName 是否不同，不同则归档到 archive/ 目录。

---

## 转换前检查清单

- [ ] 已有运行已归档
- [ ] 每个故事足够小
- [ ] 按依赖顺序排列
- [ ] 每个故事有编译检查验收标准
- [ ] 前端故事有浏览器验证验收标准
- [ ] 所有 passes 为 false，notes 为空
