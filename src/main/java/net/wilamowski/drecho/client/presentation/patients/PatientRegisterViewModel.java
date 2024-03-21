package net.wilamowski.drecho.client.presentation.patients;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.PatientVmMapper;
import net.wilamowski.drecho.connectors.model.PatientService;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations.ValidationExceptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class PatientRegisterViewModel {
  private static final Logger logger = LogManager.getLogger(PatientRegisterViewModel.class);
  private final PatientService patientService;
  private final BooleanProperty addPatientModeDisable = new SimpleBooleanProperty(true);
  private final BooleanProperty updatePatientModeDisable = new SimpleBooleanProperty(true);
  private PatientFx currentPatientFx = null;
  private PatientRegisterDataEntryMode dataEntryMode;
  private boolean isUserStartedWriting = false;

  public PatientRegisterViewModel(PatientService patientService) {
    this.patientService = patientService;
    this.currentPatientFx = PatientFx.createEmptyPatientFx();
    this.dataEntryMode = PatientRegisterDataEntryMode.READONLY;
  }

  Optional<PatientFx> registerCurrentPatient() throws ValidationExceptions {
    logger.trace("[VM] New patient registration...");
    Patient patientToRegister = PatientVmMapper.toDomain(currentPatientFx);
    try {
      Optional<Patient> createdPatient = patientService.createPatientRecord(patientToRegister);
      if (createdPatient.isPresent()) {
        PatientFx fx = PatientVmMapper.toFx(createdPatient.get());
        return Optional.of(fx);
      } else {
        return Optional.empty();
      }
    } catch (ValidationExceptions e) {
      logger.error(" [VM-PATIENT] Creating patient... FAILED");
      throw e;
    }
  }

  public PatientFx getCurrentPatientFx() {
    return currentPatientFx;
  }

  public void selectPatientForEdit(PatientFx patientFx) {
    this.currentPatientFx = patientFx;
  }

  void removePatient() {
    this.currentPatientFx = PatientFx.createEmptyPatientFx();
  }

  public void initializePeselTextField(String peselCode) {
    this.currentPatientFx.getPesel().set(peselCode);
  }

  BooleanProperty addPatientModeDisableProperty() {
    return addPatientModeDisable;
  }

  BooleanProperty updatePatientModeDisableProperty() {
    return updatePatientModeDisable;
  }

  void turnOnAddPatientMode() {
    this.dataEntryMode = PatientRegisterDataEntryMode.ADD;
    this.addPatientModeDisable.set(false);
  }

  void turnOnEditingPatientMode() {
    this.dataEntryMode = PatientRegisterDataEntryMode.EDIT;
    this.updatePatientModeDisable.set(false);
  }

  public PatientRegisterDataEntryMode dataEntryMode() {
    return dataEntryMode;
  }

  public void configureListenersForPreventingUnsavedChanges() {
    currentPatientFx
        .getName()
        .addListener((obs, oldval, newval) -> this.isUserStartedWriting = true);
    currentPatientFx
        .getLastName()
        .addListener((obs, oldval, newval) -> this.isUserStartedWriting = true);
  }

  public Optional<PatientFx> commitCurrentPatientChanges() throws ValidationExceptions {
    logger.trace("[VM] Updating patient...");
    Patient patientToRegister = PatientVmMapper.toDomain(currentPatientFx);
    try {
      Optional<Patient> createdPatient = patientService.updatePatient(patientToRegister);
      if (createdPatient.isPresent()) {
        PatientFx fx = PatientVmMapper.toFx(createdPatient.get());
        return Optional.of(fx);
      } else {
        return Optional.empty();
      }
    } catch (ValidationExceptions e) {
      logger.error(" [VM-PATIENT] Update patient... FAILED");
      throw e;
    }
  }

  public boolean isUserStartedWriting() {
    return isUserStartedWriting;
  }

  enum PatientRegisterDataEntryMode {
    READONLY,
    ADD,
    EDIT
  }
}
