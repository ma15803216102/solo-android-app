# Koi-Prd-Skill-Suite

<div align="center">

**A Complete AI-Driven PRD-to-Code Automation Suite for Trae SOLO**

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Trae SOLO](https://img.shields.io/badge/Platform-Trae%20SOLO-orange.svg)](https://trae.cn)
[![PRD Automation](https://img.shields.io/badge/PRD-Automation-green.svg)]()

[English](README.en.md) | [中文](README.md)

</div>

---

## 📖 Overview

Koi PRD Skills is a comprehensive skill suite designed for **Trae SOLO** that automates the entire workflow from **Product Requirements Document (PRD) generation** to **code implementation**. It solves the core challenges of AI-driven large-scale project development by leveraging SOLO's sub-agent capabilities and file-based context passing.

### Key Achievements

- **52 user stories delivered** in a single run
- **5 hours continuous execution** without interruption
- **Zero failures, zero retries**

## 🎯 Pain Points Solved

| Pain Point | Description |
|------------|-------------|
| **Unclear Project Boundaries** | Requirements drift during multi-turn conversations |
| **Chaotic Execution Flow** | Lack of structured workflow |
| **Context Loss** | AI forgets early requirements in long conversations |
| **Incomplete Implementation** | AI misses details from requirements specs |
| **Misleading Feedback** | AI reports don't match actual implementation |

## 🏗️ Architecture

### Workflow

```
Requirement Input
  ↓
1. PRD Generation & Validation (Multi-round Iteration)
   └── koi-prd-pipeline (Orchestrator)
       ├── koi-prd-generator → Generate PRD Draft
       │       ↓
       └── koi-prd-checker → Validate PRD Compliance
               ↓ (Not compliant)
               └── Feed back corrections to koi-prd-generator
                       ↓ (Multi-round loop until compliant)
       └── Generate user stories with proper granularity
  ↓
2. PRD Conversion
   └── koi-prd-convert-to-json → Convert to JSON
  ↓
3. Story Execution
   └── koi-prd-story-loop (Orchestrator)
       └── koi-prd-story-executor → Execute Individual Story
  ↓
Code Output
```

**Multi-round Iteration Mechanism for PRD Generation & Validation**:

`koi-prd-pipeline` serves as the orchestrator, coordinating `koi-prd-generator` and `koi-prd-checker` through multiple rounds of interaction:

1. **Generation Phase**: `koi-prd-generator` creates a PRD draft based on requirements, including user stories, acceptance criteria, dependencies, etc.
2. **Validation Phase**: `koi-prd-checker` inspects PRD compliance, including:
   - Whether user stories are granular enough (can be completed within a single context window)
   - Whether acceptance criteria are verifiable (not vague descriptions)
   - Whether dependencies are clearly marked
   - Whether story types are correctly categorized
3. **Iterative Correction**: If requirements are not met, `koi-prd-checker` outputs specific correction feedback, and `koi-prd-generator` revises the PRD accordingly
4. **Loop Until Compliance**: Repeat the generation-validation cycle until user stories meeting specifications with appropriate granularity are produced


### Skills Suite

| Skill | Function |
|-------|----------|
| [koi-prd-pipeline](koi-prd-pipeline/) | Orchestrate PRD generation & validation |
| [koi-prd-generator](koi-prd-generator/) | Generate structured PRD |
| [koi-prd-checker](koi-prd-checker/) | Validate PRD compliance |
| [koi-prd-convert-to-json](koi-prd-convert-to-json/) | Convert PRD Markdown to JSON |
| [koi-prd-story-loop](koi-prd-story-loop/) | Orchestrate story execution |
| [koi-prd-story-executor](koi-prd-story-executor/) | Execute individual user story |

## 📁 Directory Structure

```
koi-prd-skill-suite/
├── koi-prd-pipeline/          # PRD generation & validation orchestrator
│   ├── SKILL.md               # Skill definition
│   └── README.md              # Skill documentation
├── koi-prd-generator/         # PRD generator
│   ├── SKILL.md
│   └── README.md
├── koi-prd-checker/           # PRD validator
│   ├── SKILL.md
│   └── README.md
├── koi-prd-convert-to-json/   # PRD to JSON converter
│   ├── SKILL.md
│   └── README.md
├── koi-prd-story-loop/        # Story execution orchestrator
│   ├── SKILL.md
│   └── README.md
├── koi-prd-story-executor/    # Individual story executor
│   ├── SKILL.md
│   └── README.md
├── README.md                  # Chinese version
├── README.zh.md               # This file (English)
├── LICENSE                    # MIT License
├── .gitignore
├── CHANGELOG.md               # Changelog
├── CONTRIBUTING.md            # Contribution guide
└── CODE_OF_CONDUCT.md         # Code of conduct
```

### Runtime Files (Generated During Execution)

```
project-root/
└── prd-context/
    ├── prd-files/
    │   ├── prd-[feature-name].md    # PRD Markdown file
    │   └── prd-[feature-name].json  # PRD JSON with story details & status
    ├── execution-logs/
    │   └── development-log.txt      # Execution history
    └── conventions/
        └── agents.md                # Project conventions & accumulated patterns
```

## 🚀 Quick Start

### Prerequisites

- **Trae SOLO** with sub-agent support

### Usage

#### Option 1: Full Pipeline

1. Describe your project requirements to Trae SOLO
2. Invoke `koi-prd-pipeline` to generate and validate PRD
3. Invoke `koi-prd-convert-to-json` to convert PRD to JSON
4. Invoke `koi-prd-story-loop` to execute all user stories

#### Option 2: Individual Skills

You can invoke any individual skill for specific tasks:

- "Generate PRD" → `koi-prd-generator`
- "Validate PRD" → `koi-prd-checker`
- "Convert PRD to JSON" → `koi-prd-convert-to-json`
- "Execute user stories" → `koi-prd-story-loop`

## 💡 Core Design

### Sub-Agent Context Isolation

Each user story executes in a **brand-new context window** via SOLO's sub-agent feature, ensuring:

- **Focus**: Sub-agents only focus on the current story, free from historical conversation pollution
- **Efficiency**: Independent context windows enable parallel execution
- **Reliability**: File-based context passing ensures information persistence

### Pattern Accumulation

During execution, reusable patterns and best practices are accumulated in `prd-context/conventions/agents.md`, shared across all subsequent stories.

## 📊 Execution Results

### Real Project Example

```
## Execution Complete Report

- Total Stories: 52
- Completed: 52 ✅
- Failed: 0
- Total Iterations: 52 (no retries)

### Execution Timeline

| Phase                | Story Range    | Count | Status      |
|----------------------|----------------|-------|-------------|
| Scaffold             | US-000 ~ US-010| 11    | ✅ Passed   |
| Auth & Routing       | US-011 ~ US-014| 4     | ✅ Passed   |
| Approval Records     | US-015 ~ US-017| 3     | ✅ Passed   |
| Record Creation      | US-018 ~ US-020| 3     | ✅ Passed   |
| Phase 2: Cargo Info  | US-021 ~ US-025| 5     | ✅ Passed   |
| Phase 3: Transit     | US-026 ~ US-029| 4     | ✅ Passed   |
| Phase 4: Relations   | US-030 ~ US-033| 4     | ✅ Passed   |
| Refund & Approval    | US-034 ~ US-040| 7     | ✅ Passed   |
| Admin Features       | US-041 ~ US-049| 9     | ✅ Passed   |
| Logs & Details       | US-050 ~ US-051| 2     | ✅ Passed   |
```

## 🔬 Theoretical Foundation

### Academic Paper

This suite is inspired by the paper **"Context Engineering for Multi-Agent LLM Systems in Code Generation"** (arXiv:2508.08322), incorporating:

- **Intent Translator**: Clarify user requirements to ensure accurate understanding
- **Specialized Sub-agents**: Decompose complex tasks into specialized sub-agents
- **Agent Role Decomposition**: Improve maintainability through clear role division
- **Targeted Context Injection**: Solve limited context window through precise injection
- **Multi-agent Orchestration**: Automate complex tasks through sub-agent orchestration

### Open Source Project

Referenced [Ralph](https://github.com/snarktank/ralph) project's "each iteration = new context" philosophy, with significant improvements:

| Feature | Ralph | Koi PRD |
|---------|-------|---------|
| Execution Mode | Serial | Parallel |
| Flow Control | Bash scripts | AI skill orchestration |
| Dependency Management | Priority only | Explicit dependencies |
| PRD Validation | ❌ | ✅ |
| Skills Count | 2 | 6 |

## 🤝 Contributing

We welcome contributions! Please read our [Contributing Guide](CONTRIBUTING.md) for details.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Trae SOLO** for the powerful sub-agent capabilities
- [Ralph](https://github.com/snarktank/ralph) for the inspiring design philosophy
- The paper **"Context Engineering for Multi-Agent LLM Systems in Code Generation"** for theoretical foundation

---

<div align="center">

**Made with ❤️ for AI-driven development**

[↑ Back to Top](#koi-prd-skills)

</div>
