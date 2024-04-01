# Client Exception Handling

## Status

- Status: Accepted -> In progress

## Context
Need some guidelines for UI exception handlingg
## Decision
Standardize UI error handling for client exceptions to ensure consistency and improve the predictability of exception handling across the application.
## Consequences
**Pros**
- readability: enhancing readability for users and developers alike
- enhanced maintainability: Standardizing UI error handling will establish clear guidelines and practices for handling client exceptions
**Cons:**
- No immediate need for refactoring, but improving older classes would be beneficial.


## Implementation
- GeneralUiException Class: Implement a GeneralUiException class responsible for handling localized error messages. This centralized approach ensures consistent handling of errors across the application and simplifies maintenance.
- SomeSpecificException Class: developers are required to create specific exception classes extending GeneralUiException. 
These classes should hardcode error header and content message keys as private fields (e.g., e.000.header, e.000.msg) 
**AND** provide second method for more specific case.
- example of use:

check...
> if (true){
> logger.error( "[VM] Patient, performer and registrant cannot be null!");
> throw new VisitVmConfirmationException(); //or pass header and message
> }

and...

> } catch (VisitVmConfirmationException ve){
> ExceptionAlert alert = ExceptionAlert.create();
> alert.showError( ve, ve.getHeader(), ve.getContent() );
> }  
