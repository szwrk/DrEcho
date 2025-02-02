package net.wilamowski.drecho.app.dto;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
    @ToString
    @Builder
    @Getter
    public class PatientDto {
        private final String name;
        private final String lastName;
        @ToString.Exclude private final String pesel;
        private Long id;
        private String nameOfCityBirth;
        private String codeOfCityBirth;
        private LocalDate dateBirth;
        private String generalPatientNote;
        private String patientTelephoneNumber;
}
