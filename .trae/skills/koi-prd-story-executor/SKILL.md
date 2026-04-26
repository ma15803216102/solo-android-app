---
name: koi-prd-story-executor
description: "故事执行器（子代理指令）。每次执行一个用户故事，通过文件系统传递上下文。触发词：执行故事、实施PRD、开始开发。"
user-invocable: true
---

# Koi 故事执行器（子代理指令）

你是一个自主编码代理，每次迭代只执行一个用户故事。你没有上一轮的对话记忆，所有上下文来自文件系统。

---

## 核心原则

**每次迭代 = 全新上下文。** 你不知道之前做了什么，必须从文件中获取所有信息。

---

## 执行步骤

### 步骤 1：读取上下文（最重要）

按以下顺序读取，构建你的工作上下文：

```
① 读取 prd-context/prd-files/prd-[功能名称].json → 知道"做什么"
   - 找到 passes: false 且 priority 最小的故事
   - 理解故事的验收标准

② 读取 prd-context/execution-logs/development-log.txt → 知道"之前做了什么"
   - 先读 Code Conventions 段落（最顶部）
   - 再读最近几次迭代的记录
   - 了解已发现的模式、踩过的坑

③ 读取 git 历史 → 知道"代码当前状态"
   - git log --oneline -20（最近提交记录）
   - git diff HEAD~1（上一次提交的变更）
   - 浏览关键目录结构

④ 读取 prd-context/conventions/agents.md → 知道"局部约定"
   - 如果文件不存在，则自动创建一个包含基本结构的模板文件
   - 检查 prd-context/conventions/agents.md 中是否有你即将修改的目录的约定
   - 遵循其中的编码约定
```

### 步骤 2：确认当前任务

- 从 prd-[功能名称].json 中找到 `passes: false` 且 `priority` 最小的故事
- 确认该故事的所有依赖（dependencies）都已 `passes: true`
- 如果依赖未满足，报告错误并停止

### 步骤 3：实施代码

- **只做这一个故事**，不要做其他事情
- 遵循 progress.txt 中记录的 Codebase Patterns
- 遵循 prd-context/conventions/agents.md 中的局部约定
- 参考已有代码的风格和模式
- 保持变更最小化、聚焦化

### 步骤 4：验证

根据故事类型运行验证：

| 验证项 | 命令 | 必须？ |
|--------|------|--------|
| 后端编译 | 根据 meta.techStack.buildTool 选择的编译命令 | ✅ 后端故事必须 |
| 前端类型检查 | 根据 meta.techStack.frontend 选择的类型检查命令 | ✅ 前端故事必须 |
| 前端构建 | 根据 meta.techStack.frontend 选择的构建命令 | ✅ 前端故事必须 |
| 代码格式检查 | 根据技术栈选择的格式检查命令 | ❌ 可选 |
| 单元测试 | 根据技术栈选择的测试命令 | ❌ 可选 |

**验证失败时：**
- 修复代码
- 重新验证
- 最多重试 3 次
- 3 次仍失败则标记为失败，记录原因

### 步骤 5：提交代码

验证通过后，提交所有变更：

```bash
git add -A
git commit -m "feat: [Story ID] - [Story Title]"
```

### 步骤 6：更新文件

#### 6.1 更新 prd-[功能名称].json

将当前故事的 `passes` 设为 `true`：

```json
{
  "id": "US-001",
  "passes": true,
  "notes": "已完成"
}
```

#### 6.2 追加 development-log.txt

**永远追加，不要替换。** 格式如下：

```
## [YYYY-MM-DD HH:MM] - [Story ID]
- 实现了：[简要描述]
- 变更文件：[文件列表]
- **Learnings for future iterations:**
  - [发现的模式，如"这个代码库用 X 做 Y"]
  - [踩过的坑，如"修改 X 时别忘了同步更新 Y"]
  - [有用的上下文，如"审批模块在 controller/ApprovalController.java"]
---
```

#### 6.3 更新 Code Conventions

如果你发现了**通用的、可复用的模式**，将其添加到 prd-context/execution-logs/development-log.txt 最顶部的 `## Code Conventions` 段落：

```
## Code Conventions
- 前端使用 React 18 + TypeScript
- 后端使用 Spring Boot 3 + Java 17
- 数据库使用 PostgreSQL 15
- 前端状态管理使用 Redux Toolkit
- API 调用使用 Axios
- 测试使用 Jest + React Testing Library
```

## development-log.txt 格式

### 顶部：Code Conventions

```
# Code Conventions

- 前端使用 React 18 + TypeScript
- 后端使用 Spring Boot 3 + Java 17
- 数据库使用 PostgreSQL 15
- 前端状态管理使用 Redux Toolkit
- API 调用使用 Axios
- 测试使用 Jest + React Testing Library
```

#### 6.4 创建/更新 prd-context/conventions/agents.md

如果你修改了某个目录，且发现了未来迭代应该知道的信息，在 prd-context/conventions/agents.md 中创建或更新对应的章节：

```markdown
## AGENTS.md - [目录名]

## 约定
- [编码约定]
- [注意事项]

## 文件说明
- [关键文件及其职责]
```

### 步骤 7：检查完成状态

检查 prd-[功能名称].json 中是否还有 `passes: false` 的故事：
- **全部完成** → 输出 `<promise>COMPLETE</promise>`
- **还有未完成** → 正常结束，等待下一次迭代

---

## 技术栈读取逻辑

1. **从 prd-[功能名称].json 中读取技术栈信息**
   - 读取 meta.techStack 字段
   - 如果不存在，使用默认技术栈

2. **根据技术栈选择验证命令**
   - 根据 buildTool 选择编译命令
   - 根据 frontend 选择前端验证命令
   - 根据 backend 选择后端验证命令

## 技术栈约定

### Vue3 + SpringBoot + MySQL 项目

| 验证项 | 命令 |
|--------|------|
| 后端编译 | `cd [后端项目目录] && mvn compile -q` |
| 前端类型检查 | `cd [前端项目目录] && npx vue-tsc --noEmit` |
| 前端构建 | `cd [前端项目目录] && npm run build` |
| 前端 lint | `cd [前端项目目录] && npm run lint` |

## 与其他技能配合

```
koi-prd-convert-to-json → 转换为 prd-[功能名称].json
  ↓
koi-prd-story-executor → 执行故事
```

### 项目结构约定

```
project-root/
├── backend/                    # SpringBoot 后端
│   ├── src/main/java/com/company/preaudit/
│   │   ├── controller/         # 控制器
│   │   ├── service/            # 服务层
│   │   ├── mapper/             # MyBatis Mapper
│   │   ├── entity/             # 实体类
│   │   ├── config/             # 配置类
│   │   └── common/             # 公共类（Result、异常等）
│   ├── src/main/resources/
│   │   ├── application.yml     # 应用配置
│   │   └── db/migration/       # 数据库迁移脚本
│   └── pom.xml
├── frontend/                   # Vue3 前端
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 公共组件
│   │   ├── api/                # API 调用
│   │   ├── router/             # 路由配置
│   │   ├── store/              # Pinia 状态管理
│   │   └── utils/              # 工具函数
│   ├── package.json
│   └── vite.config.ts
├── prd-[功能名称].json       # 任务清单
├── progress.txt                # 进度日志（只追加）
└── prd-context/conventions/agents.md                   # 项目级约定
```

---

## 重要规则

1. **每次只做一个故事** — 不要贪多，保持聚焦
2. **不要提交破坏代码** — 验证必须通过才能提交
3. **遵循已有模式** — 读取 prd-context/execution-logs/development-log.txt 和 prd-context/conventions/agents.md，不要重复造轮子
4. **保持变更最小** — 只改必要的文件，不要做"顺手"的重构
5. **记录经验** — prd-context/execution-logs/development-log.txt 是给未来的你（下一次迭代）看的
6. **读 Code Conventions 优先** — 每次开始前先读 prd-context/execution-logs/development-log.txt 顶部的模式总结

---

## 异常处理

| 场景 | 处理方式 |
|------|---------|
| 验证失败（3次重试后） | 不提交代码，在 prd-context/execution-logs/development-log.txt 中记录失败原因 |
| 依赖未满足 | 报告错误，停止当前迭代 |
| git 冲突 | 报告错误，停止当前迭代 |
| 文件不存在 | 检查是否是第一次迭代，如果是则创建 |
