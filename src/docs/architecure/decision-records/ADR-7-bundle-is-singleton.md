---
id: 7
layout: "adr.njk"
tags: ["adr"]
title: "ResourceBundle as singleton"
status: "Accepted -> In use"
date: 2024-01-01
context: |
  Dependency injection of bundle instances is inconvenient.
  The client always has one bundle pack for the current instance.

decision: |
  Utilize a static class to manage ResourceBundle instances.
  Wrap the ResourceBundle class functionality.
  This decision allows for easier access and management of ResourceBundle instances within the application.

pros: 
  - Simplifies access and management of ResourceBundle instances.

cons: 
  - None

consequences: "Improves management of ResourceBundle instances but requires careful handling of static resources."
---
