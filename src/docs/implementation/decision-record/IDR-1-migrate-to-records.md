# Title: Migrate repository entities to Java Records feature
## Context
Frontend forced newer Java version, so is possible to simplify some weak repo classes.
## Decision
Migrate class annotation by lombok to Java Records feature

## Consequences

### Pros
- Simpler, clean, native solution.
- Easiest testable (now without Lombok lib, explicit method invoking is possible)
### Cons
- Some work to do
## Alternatives Considered
n/d

## Status
Used

