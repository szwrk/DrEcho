---
id: 6
layout: "adr.njk"
tags: ["adr"]
title: "Bundle naming convention"
status: "Accepted -> In use"
date: 2024-01-01
context: "Need to standardize bundle key conventions."

decision: |
  Use pattern like this:
  <module>.<feature>.<action>
  
  Example:
  `ui.patient.record.save-patient-button=someValue`

pros: 
  - Standardized and clear naming conventions.

cons: 
  - None

consequences: "Helps ensure consistency and clarity across the project when defining bundle keys."
---
