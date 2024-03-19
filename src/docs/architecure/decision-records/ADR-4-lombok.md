# Use Lombok (constraints!)
## Status
- Accepted->In use->Update(reason: migrate to newer JDK, use records in repo layers)
## Context
- weak entity code handling
- difficulty in maintaining current classes durring functionality implemenatation
## Decision
- Lombok can only be used in the client view layer
- services and repositories will utilize Java Records
## Consequences

**Pros:**
- improved readability
- enhanced maintainability
- core domain remains clean

**Cons:**
- additional dependency
- **important:** default @toString for joins in JPA can be dangerous
- **important:** be careful with sensitive data classes and @toString (logging etc) 