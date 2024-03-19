# ResourceBundle as singleton
## Status
- Date: 06/01/2024
- Accepted->**In use**
## Context
Dependency injection of bundle instances is inconvenient.
Client always has one bundle pack for the current instance.
## Decision
Utilize a static class to manage ResourceBundle instances.
Wrap the ResourceBundle class functionality.
This decision allows for easier access and management of ResourceBundle instances within the application.
## Consequences
**Pros:**
-

**Cons:**
-