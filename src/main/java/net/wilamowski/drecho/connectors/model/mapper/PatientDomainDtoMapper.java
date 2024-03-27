package net.wilamowski.drecho.connectors.model.mapper;
/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.shared.dto.PatientDto;

public class PatientDomainDtoMapper {
    public static PatientDto toDto(Patient domainObj) {
        return PatientDto.builder()
                .name(domainObj.getName())
                .lastName(domainObj.getLastName())
                .pesel(domainObj.getPesel())
                .id(domainObj.getId())
                .nameOfCityBirth(domainObj.getNameOfCityBirth())
                .codeOfCityBirth(domainObj.getCodeOfCityBirth())
                .dateBirth(domainObj.getDateBirth())
                .generalPatientNote(domainObj.getGeneralPatientNote())
                .patientTelephoneNumber(domainObj.getPatientTelephoneNumber())
                .build();
    }

    public static Patient toDomain(PatientDto dto) {
        return Patient.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .pesel(dto.getPesel())
                .id(dto.getId())
                .nameOfCityBirth(dto.getNameOfCityBirth())
                .codeOfCityBirth(dto.getCodeOfCityBirth())
                .dateBirth(dto.getDateBirth())
                .generalPatientNote(dto.getGeneralPatientNote())
                .patientTelephoneNumber(dto.getPatientTelephoneNumber())
                .build();
    }
}
