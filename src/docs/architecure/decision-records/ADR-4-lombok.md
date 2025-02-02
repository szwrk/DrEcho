---
id: 4
layout: "adr.njk"
tags: ["adr"]
title: "Use Lombok (constraints!)"
status: "Accepted -> In use -> Update (Reason: migrate to newer JDK, use records in repo layers)"
date: 2024-01-01
context: |
  - Weak entity code handling.
  - Difficulty in maintaining current classes during functionality implementation.

decision: |
  - Lombok can only be used in the client view layer.
  - Services and repositories will utilize Java Records.

pros: 
  - Improved readability.
  - Enhanced maintainability.
  - Core domain remains clean.

cons: 
  - Additional dependency.
  - Important: Default `@toString` for joins in JPA can be dangerous.
  - Important: Be careful with sensitive data classes and `@toString` (logging, etc).

consequences: "Ensures cleaner domain layers but introduces careful handling of sensitive data when using Lombok's `@toString`."
---
