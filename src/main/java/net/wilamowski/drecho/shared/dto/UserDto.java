package net.wilamowski.drecho.shared.dto;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class UserDto {
    private final String login;
    private final String password;
    private String firstName;
    private String surname;
    private String titlePrefix;
    private String specialization; // specialization, function...
    private String licenceNumber;
    private Boolean isBlocked;
}
