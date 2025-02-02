---
id: 9
layout: "adr.njk"
tags: ["adr"]
title: "Client Exception Handling"
status: "Accepted -> In progress"
date: 2024-01-01
context: "Need some guidelines for UI exception handling."
decision: |
  Standardize UI error handling for client exceptions to ensure consistency and improve the predictability of exception handling across the application.
pros: 
  - "Enhances readability for users and developers."
  - "Improves maintainability by standardizing UI error handling."
cons: 
  - "No immediate need for refactoring, but improving older classes would be beneficial."
---
# Implementation

- **GeneralUiException Class**: Implement a `GeneralUiException` class responsible for handling localized error messages. This centralized approach ensures consistent handling of errors across the application and simplifies maintenance.
  
- **SomeSpecificException Class**: Developers are required to create specific exception classes extending `GeneralUiException`. These classes should hardcode error header and content message keys as private fields (e.g., `e.000.header`, `e.000.msg`) **AND** provide a second method for more specific cases.

## Example of use:

```java
// Check...
if (true) {
    logger.error("[VM] Patient, performer and registrant cannot be null!");
    throw new VisitVmConfirmationException(); //or pass header and message
}

// Catch...
} catch (VisitVmConfirmationException ve) {
    ExceptionAlert alert = ExceptionAlert.create();
    alert.showError(ve, ve.getHeader(), ve.getContent());
}
```