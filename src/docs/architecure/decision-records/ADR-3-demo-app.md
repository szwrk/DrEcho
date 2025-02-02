---
id: 3
layout: "adr.njk"
tags: ["adr"]
title: "Demo implementing for MVP version"
status: "Accepted"
date: 2024-01-01
context: >
  Need for demo implementation for:
  - presentation MVP
  - testing

decision: |
  - Implement standalone service layer with in-memory fake repositories.
  - Implement functionality to switch initializing with random business demo data.

pros: 
  - Supports Minimal Viable Product (MVP) demo.
  - Enhances the testability of the application (unit tests, e2e).
  - Provides manual frontend tests without using a database.
  - Validates switchable service layer without needing a database.

cons: 
  - None

consequences: "Enables easier testing and demonstration of MVP without needing a fully implemented backend."
---
