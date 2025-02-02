package net.wilamowski.drecho.app.dto;
/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record VisitDtoCreate(
    String registrantLogin,
    String performerLogin,
    LocalDateTime realizationDateTime,
    LocalDateTime viewStartDateTime,
    Long patientId) {
}
