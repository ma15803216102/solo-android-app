# Koi-Prd-Skill-Suite

<div align="center">

**一套为 Trae SOLO 设计的 AI 驱动 PRD 到代码落地自动化技能套件**

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Trae SOLO](https://img.shields.io/badge/Platform-Trae%20SOLO-orange.svg)](https://trae.cn)
[![PRD Automation](https://img.shields.io/badge/PRD-Automation-green.svg)]()

[English](README.en.md) | [中文](README.md)

</div>

---

## 📖 概述

Koi PRD Skills 是一套为 **Trae SOLO** 设计的完整技能套件，自动化从**产品需求文档（PRD）生成**到**代码落地**的全流程。它利用 SOLO 的子代理能力和基于文件的上下文传递机制，解决了 AI 驱动大型项目开发的核心挑战。

### 核心成果

- 单次运行交付 **52 个用户故事**
- 连续运行 **5 小时**不中断

## 🎯 解决的痛点

| 痛点 | 描述 |
|------|------|
| **项目边界不清** | 多轮对话中需求不断变化，AI 肆意扩展功能 |
| **执行流程混乱** | 缺乏结构化的执行流程，代码质量不可控 |
| **上下文丢失** | 长对话中 AI 遗忘早期需求，重复工作 |
| **实现不完整性** | AI 未完全覆盖需求规格中的所有细节 |
| **误导性反馈** | AI 报告与实际实现情况不符 |

## 🏗️ 架构

### 工作流程

```
需求输入
  ↓
1. PRD 生成与校验阶段（多轮迭代）
   └── koi-prd-pipeline（编排器）
       ├── koi-prd-generator → 生成 PRD 初稿
       │       ↓
       └── koi-prd-checker → 校验 PRD 规范性
               ↓（不符合要求）
               └── 反馈修正意见给 koi-prd-generator
                       ↓（多轮循环直到符合要求）
       └── 生成符合粒度要求的用户故事
  ↓
2. PRD 格式转换阶段
   └── koi-prd-convert-to-json → 转换为 JSON
  ↓
3. 故事执行阶段
   └── koi-prd-story-loop（编排器）
       └── koi-prd-story-executor → 执行单个故事
  ↓
代码产出
```

**PRD 生成与校验的多轮迭代机制**：

`koi-prd-pipeline` 作为编排器，协调 `koi-prd-generator` 和 `koi-prd-checker` 进行多轮交互：

1. **生成阶段**：`koi-prd-generator` 根据需求生成 PRD 初稿，包含用户故事、验收标准、依赖关系等
2. **校验阶段**：`koi-prd-checker` 检查 PRD 规范性，包括：
   - 用户故事粒度是否足够小（能在单个上下文窗口内完成）
   - 验收标准是否可验证（非模糊描述）
   - 依赖关系是否明确标注
   - 故事类型是否正确分类
3. **迭代修正**：如不符合要求，`koi-prd-checker` 输出具体修正意见，`koi-prd-generator` 据此修正 PRD
4. **循环直至达标**：重复生成-校验循环，直到产出符合规范、粒度合适的用户故事列表

### 技能套件

| 技能 | 功能 |
|------|------|
| [koi-prd-pipeline](koi-prd-pipeline/) | 编排 PRD 生成与校验 |
| [koi-prd-generator](koi-prd-generator/) | 生成结构化 PRD |
| [koi-prd-checker](koi-prd-checker/) | 校验 PRD 规范性 |
| [koi-prd-convert-to-json](koi-prd-convert-to-json/) | 将 PRD Markdown 转换为 JSON |
| [koi-prd-story-loop](koi-prd-story-loop/) | 编排故事执行 |
| [koi-prd-story-executor](koi-prd-story-executor/) | 执行单个用户故事 |

## 📁 目录结构

```
koi-prd-skill-suite/
├── koi-prd-pipeline/          # PRD 生成与校验编排器
│   ├── SKILL.md               # 技能定义
│   └── README.md              # 技能文档
├── koi-prd-generator/         # PRD 生成器
│   ├── SKILL.md
│   └── README.md
├── koi-prd-checker/           # PRD 校验器
│   ├── SKILL.md
│   └── README.md
├── koi-prd-convert-to-json/   # PRD 转 JSON
│   ├── SKILL.md
│   └── README.md
├── koi-prd-story-loop/        # 故事执行编排器
│   ├── SKILL.md
│   └── README.md
├── koi-prd-story-executor/    # 单个故事执行器
│   ├── SKILL.md
│   └── README.md
├── README.md                  # 本文件（中文）
├── README.zh.md               # 英文版本
├── LICENSE                    # MIT 许可证
├── .gitignore
├── CHANGELOG.md               # 变更日志
├── CONTRIBUTING.md            # 贡献指南
└── CODE_OF_CONDUCT.md         # 行为准则
```

### 运行时文件（执行过程中生成）

```
project-root/
└── prd-context/
    ├── prd-files/
    │   ├── prd-[功能名称].md    # PRD Markdown 文件
    │   └── prd-[功能名称].json  # PRD JSON 文件（含故事详情、依赖关系和执行状态）
    ├── execution-logs/
    │   └── development-log.txt  # 执行历史记录
    └── conventions/
        └── agents.md            # 项目约定与沉淀的模式（执行过程中持续积累，供后续故事共享）
```

## 🚀 快速开始

### 前置条件

- 支持子代理的 **Trae SOLO**

### 使用方法

#### 方式一：完整流程

1. 向 Trae SOLO 描述你的项目需求
2. 调用 `koi-prd-pipeline` 生成并校验 PRD
3. 调用 `koi-prd-convert-to-json` 转换 PRD 为 JSON
4. 调用 `koi-prd-story-loop` 执行所有用户故事

#### 方式二：单独使用技能

你可以调用任何一个技能完成特定任务：

- "生成 PRD" → `koi-prd-generator`
- "校验 PRD" → `koi-prd-checker`
- "转换 PRD 为 JSON" → `koi-prd-convert-to-json`
- "执行用户故事" → `koi-prd-story-loop`

## 💡 核心设计

### 子代理上下文隔离

每个用户故事通过 SOLO 的子代理功能在**全新的上下文窗口**中执行，确保：

- **聚焦性**：子代理只关注当前故事，不受历史对话污染
- **高效性**：独立上下文窗口支持并行执行
- **可靠性**：基于文件的上下文传递确保信息持久化

### 模式沉淀

执行过程中，可复用的模式和最佳实践会沉淀到 `prd-context/conventions/agents.md` 中，供后续所有故事共享。

## 📊 执行结果

### 真实项目示例

```
## 执行完成报告

- 总故事数：52
- 已完成：52 ✅
- 失败：0
- 总迭代次数：52（无重试）

### 执行时间线

| 阶段 | 故事范围 | 数量 | 状态 |
|------|---------|------|------|
| Scaffold | US-000 ~ US-010 | 11 | ✅ 全部通过 |
| 认证与路由 | US-011 ~ US-014 | 4 | ✅ 全部通过 |
| 审批记录列表 | US-015 ~ US-017 | 3 | ✅ 全部通过 |
| 记录创建+阶段一 | US-018 ~ US-020 | 3 | ✅ 全部通过 |
| 阶段二货物信息 | US-021 ~ US-025 | 5 | ✅ 全部通过 |
| 阶段三运输信息 | US-026 ~ US-029 | 4 | ✅ 全部通过 |
| 阶段四关联信息 | US-030 ~ US-033 | 4 | ✅ 全部通过 |
| 退款+审批 | US-034 ~ US-040 | 7 | ✅ 全部通过 |
| 管理员功能 | US-041 ~ US-049 | 9 | ✅ 全部通过 |
| 日志+详情 | US-050 ~ US-051 | 2 | ✅ 全部通过 |
```

## 🔬 理论基础

### 学术论文

本套件的设计思想深受论文《Context Engineering for Multi-Agent LLM Systems in Code Generation》（arXiv:2508.08322）的启发，融合了：

- **意图翻译器**：通过澄清用户需求，确保需求理解准确
- **专业化子代理**：将复杂任务分解为多个专业化子代理
- **代理角色分解**：通过明确的代理角色划分，提高系统的可维护性
- **目标上下文注入**：通过精准的上下文注入，解决AI上下文窗口有限的问题
- **多代理编排**：通过编排多个专业化子代理，实现复杂任务的自动化执行

### 开源项目

参考了 [Ralph](https://github.com/snarktank/ralph) 项目"每次迭代=全新上下文"的核心理念，并进行了重要改进：

| 特性 | Ralph | Koi PRD |
|------|-------|---------|
| 执行模式 | 串行 | 并行 |
| 流程控制 | Bash 脚本 | AI 技能编排 |
| 依赖管理 | 仅优先级 | 明确依赖关系 |
| PRD 校验 | ❌ | ✅ |
| 技能数量 | 2 | 6 |

## 🤝 贡献

欢迎贡献！请阅读我们的[贡献指南](CONTRIBUTING.md)了解详情。

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 🙏 致谢

- 感谢 **Trae SOLO** 提供的强大子代理能力
- 感谢 [Ralph](https://github.com/snarktank/ralph) 的设计思想启发
- 感谢论文《Context Engineering for Multi-Agent LLM Systems in Code Generation》提供的理论基础

---

<div align="center">

**为 AI 驱动开发而打造 ❤️**

[↑ 回到顶部](#koi-prd-skills)

</div>


**为 AI 驱动开发而打造 ❤️**

</div>
