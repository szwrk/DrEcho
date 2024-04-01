package net.wilamowski.drecho.shared.dto; /*Author:
                                          Arkadiusz Wilamowski
                                          https://github.com/szwrk
                                          */

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record VisitDtoResponse(
            Long visitId,
            String registrantLogin,
            String performerLogin,
            LocalDateTime realizationDateTime,
            LocalDateTime viewStartDateTime,
            Long patientId) {
}
