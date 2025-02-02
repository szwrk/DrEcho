package net.wilamowski.drecho.app.dto;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

import lombok.Builder;

@Builder
public record UserDto(
    String login,
    String password,
    String firstName,
    String surname,
    String titlePrefix,
    String specialization,
    String licenceNumber,
    Boolean isBlocked) {}
