---
id: 2
layout: "adr.njk"
tags: ["adr"]
title: "Use AtlantaFx for styling"
status: "Accepted"
date: 2024-01-01
context: "Deciding between MaterialFx and AtlantaFx (GH Primer implementation) for styling."
decision: "Choosing AtlantaFx, because it works fine, looks good, and doesn't add dependency like MaterialFx."
pros: 
  - "Provides visually appealing UI elements."
  - "Enhances user experience."
  - "Does not introduce additional dependencies."
cons: 
  - "Smaller community compared to MaterialFx."
examples: |
  ### MVVM - Layer dependencies

  #### View

  - View can use ViewModel classes
  - Bindings of nodes and controls

  #### ViewModel

  - View state
  - Invoking logic
  - Input single validation
consequences: "Easier integration with frontend technologies, but smaller community support."
---
