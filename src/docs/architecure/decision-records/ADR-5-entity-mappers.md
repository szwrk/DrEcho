---
id: 5
layout: "adr.njk"
tags: ["adr"]
title: "Clean architecture"
status: "Accepted -> In use"
date: 2024-01-01
context: |
  - The app is growing.
  - Prevents tight coupling between layers for easier maintenance and testing.
  - Helps clean up layers.

decision: "Use ArchUnit to enforce clean architecture."

pros: 
  - Easier maintenance and testing.
  - Decoupling of layers.

cons: 
  - None

consequences: "Promotes scalable architecture by keeping layers clean and decoupled."
---
