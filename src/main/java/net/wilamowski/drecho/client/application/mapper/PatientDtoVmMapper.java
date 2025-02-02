package net.wilamowski.drecho.client.application.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.app.dto.PatientDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class PatientDtoVmMapper {
  private static final Logger logger = LogManager.getLogger( PatientDtoVmMapper.class);

  public static List<PatientVM> toListToFx(List<PatientDto> fetchedPatients) {
    return fetchedPatients.stream().map( PatientDtoVmMapper::toVm ).collect(Collectors.toList());
  }

  public static PatientVM toVm(PatientDto patient) throws NullPointerException {
    assert patient != null : "Error occurred during mapping: patientFx object is null";
    return PatientVM.builder()
        .id(new SimpleLongProperty(patient.getId()))
        .name(new SimpleStringProperty(patient.getName()))
        .lastName(new SimpleStringProperty(patient.getLastName()))
        .pesel(new SimpleStringProperty(patient.getPesel()))
        .nameOfCityBirth(new SimpleStringProperty(patient.getNameOfCityBirth()))
        .codeOfCityBirth(new SimpleStringProperty(patient.getCodeOfCityBirth()))
        .patientTelephoneNumber(new SimpleStringProperty(patient.getPatientTelephoneNumber()))
        .generalPatientNote(new SimpleStringProperty(patient.getGeneralPatientNote()))
        .dateBirth(new SimpleObjectProperty<>(patient.getDateBirth()))
        .build();
  }
  public static PatientDto toDto(PatientVM patientVM) {
    Objects.requireNonNull(patientVM, "Error occurred during mapping: patientVM object is null");

    return PatientDto.builder()
            .name(patientVM.getName().get())
            .lastName(patientVM.getLastName().get())
            .pesel(patientVM.getPesel().get()) // Assuming you want to include pesel
            .id(patientVM.getId().get())
            .nameOfCityBirth(patientVM.getNameOfCityBirth().get())
            .codeOfCityBirth(patientVM.getCodeOfCityBirth().get())
            .dateBirth(patientVM.getDateBirth().get())
            .generalPatientNote(patientVM.getGeneralPatientNote().get())
            .patientTelephoneNumber(patientVM.getPatientTelephoneNumber().get())
            .build();
  }
}
