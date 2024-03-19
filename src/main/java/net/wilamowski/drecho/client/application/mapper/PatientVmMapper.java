package net.wilamowski.drecho.client.application.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */

public class PatientVmMapper {
  private static final Logger logger = LogManager.getLogger( PatientVmMapper.class);

  public static List<PatientFx> toListToFx(List<Patient> fetchedPatients) {
    return fetchedPatients.stream().map( PatientVmMapper::toFx).collect(Collectors.toList());
  }

  public static PatientFx toFx(Patient patient) throws NullPointerException {
    assert patient != null : "Error occurred during mapping: patientFx object is null";
    return PatientFx.builder()
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

  public static Patient toDomain(PatientFx patientFx) {
    Objects.requireNonNull(patientFx, "Error occurred during mapping: patientFx object is null");
    return Patient.builder()
        .id(patientFx.getId().getValue())
        .name(patientFx.getName().getValueSafe())
        .lastName(patientFx.getLastName().getValueSafe())
        .pesel(patientFx.getPesel().getValueSafe())
        .nameOfCityBirth(patientFx.getNameOfCityBirth().getValueSafe())
        .codeOfCityBirth(patientFx.getCodeOfCityBirth().getValueSafe())
        .patientTelephoneNumber(patientFx.getPatientTelephoneNumber().getValueSafe())
        .generalPatientNote(patientFx.getGeneralPatientNote().getValueSafe())
        .dateBirth(patientFx.getDateBirth().getValue())
        .build();
  }
}
