# 国际化 IAM 项目 — 项目总纲

## 项目定位

这是一个公开展示用的 IAM（身份与访问管理）系统项目。

该项目：

* 不复制公司项目
* 不使用任何内部代码或数据
* 基于通用 IAM 设计思想构建

目标是打造一个“可讲清楚、可展示、可扩展”的现代 IAM 系统。

---

## 项目目标

* 提升现代 Java 后端能力
* 深入理解 IAM / 安全体系
* 掌握 Docker 本地开发
* 学习 GitLab CI/CD
* 打造海外求职作品集
* 提升技术英文表达能力

---

## 架构设计

采用 **单体模块化架构（Modular Monolith）**：

* application（入口）
* authentication（认证）
* authorization（授权）
* tenant（租户）
* mfa（多因素认证）
* scim（标准接口）
* audit（审计）
* common（公共模块）

---

## 技术栈

* Java 21（17 兼容）
* Spring Boot 3
* Spring Security
* Spring Authorization Server
* PostgreSQL
* Redis
* Flyway
* Testcontainers
* Docker / Compose
* Maven Wrapper

---

## 开发原则

* 先简单再复杂
* 不提前引入复杂安全逻辑
* 先建模型，再做功能
* AI 代码必须人工理解
* 设计必须可讲清楚

---

## MVP 路线

1. 基线（已完成）
2. 核心模型（当前阶段）
3. 数据库迁移
4. OAuth2
5. JWT / OIDC
6. RBAC
7. 审计
8. MFA
9. SCIM
10. 文档
11. CI/CD

---

## 安全原则

* 禁止明文密码
* 禁止明文 client secret
* token 必须谨慎处理
* 日志不能泄露敏感信息

---

## 非目标

* 不是生产系统
* 不是企业 IAM 替代品
* 不做复杂云原生系统

---

## 最终目标

一个：

* 架构清晰
* 可演示
* 可讲解
* 可扩展

的 IAM 系统
