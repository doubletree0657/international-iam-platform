# International IAM Platform — Project Vision

## 1. Project Overview

This project is a public portfolio IAM (Identity and Access Management) platform designed to demonstrate modern backend engineering, security concepts, and system design.

It is NOT a copy of any company system and does not use any proprietary code or data.

The goal is to build a clean, understandable, and production-inspired IAM system from scratch.

---

## 2. Goals

* Improve modern Java backend skills (Java 21, Spring Boot 3)
* Learn identity and access management concepts
* Practice Docker-based local development
* Gain CI/CD experience using GitLab
* Build a strong international portfolio project
* Improve technical English communication

---

## 3. Architecture

The system follows a **modular monolith architecture**:

* application
* authentication
* authorization
* tenant
* mfa
* scim
* audit
* common

Each module is logically separated but runs in a single Spring Boot application.

---

## 4. Technology Stack

* Java 21 (Java 17 fallback)
* Spring Boot 3.5.x
* Spring Security
* Spring Authorization Server
* PostgreSQL
* Redis
* Flyway
* Testcontainers
* Docker & Docker Compose
* Maven Wrapper
* GitHub (portfolio)
* GitLab (CI/CD practice)

---

## 5. Development Principles

* Start simple, evolve gradually
* Avoid over-engineering
* Do not introduce security complexity too early
* Always design domain models before features
* Every AI-generated change must be reviewed
* Keep the system explainable in interviews

---

## 6. MVP Roadmap

1. Project baseline (done)
2. Core domain model (current)
3. Database migration (Flyway)
4. OAuth2 Authorization Server
5. OIDC / JWT
6. RBAC
7. Audit logging
8. MFA (TOTP)
9. SCIM APIs
10. OpenAPI docs
11. CI/CD pipeline

---

## 7. Security Principles

* Never store secrets in plain text
* Passwords must be hashed
* Client secrets must be encrypted
* Tokens must be handled carefully
* Logs must not leak sensitive data

---

## 8. Non-Goals

* Not a production-ready system
* Not a full enterprise IAM replacement
* Not a clone of any existing product

---

## 9. Target Outcome

A clean, well-structured IAM system that can be:

* Demonstrated in interviews
* Explained clearly
* Extended step-by-step
