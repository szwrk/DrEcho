package net.wilamowski.drecho.client.presentation.patients;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.PatientDtoVmMapper;
import net.wilamowski.drecho.configuration.backend_ports.PatientService;
import net.wilamowski.drecho.standalone.domain.patient.validations.ValidationExceptions;
import net.wilamowski.drecho.app.dto.PatientDto;
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
  private final BooleanProperty isEditPatientMode = new SimpleBooleanProperty(true);
  private final BooleanProperty disableCitizenCodeField = new SimpleBooleanProperty(false);
  private PatientVM currentPatientVM = null;
  private PatientRegisterDataEntryMode dataEntryMode;
  private boolean isUserStartedWriting = false;

  private final ChangeListener<String> nameListener =  (obs, oldval, newval) -> {
    changeFlagIfNewValueIsNotEmpty( newval );
  };
  private final ChangeListener<String> lastNameListener = (obs, oldval, newval) -> {
    changeFlagIfNewValueIsNotEmpty( newval );
  };
  private void changeFlagIfNewValueIsNotEmpty(String newval) {
    if ( newval != null) {
      if ( newval.isEmpty()) {
        this.isUserStartedWriting = false;
      } else {
        this.isUserStartedWriting = true;
      }
    } else {
      this.isUserStartedWriting = false;
    }
  }


  public PatientRegisterViewModel(PatientService patientService) {
    this.patientService = patientService;
    this.currentPatientVM = PatientVM.createEmptyPatientFx();
    this.dataEntryMode = PatientRegisterDataEntryMode.READONLY;
  }

  Optional<PatientVM> registerCurrentPatient() throws ValidationExceptions {
    logger.trace("[VM] New patient registration... START");
    PatientDto patientToRegister = PatientDtoVmMapper.toDto( currentPatientVM );
    try {
      Optional<PatientDto> createdPatient = patientService.createPatientRecord(patientToRegister);
      if (createdPatient.isPresent()) {
        PatientVM createdPatentVm = PatientDtoVmMapper.toVm(createdPatient.get());
        removeCurrentPatient( );
        logger.trace("[VM] New patient registration... DONE");
        return Optional.of(createdPatentVm);
      } else {
        logger.trace("[VM] New patient registration... FAILED");
        return Optional.empty();
      }
    } catch (ValidationExceptions e) {
      logger.error(" [VM-PATIENT] New patient registration... FAILED");
      logger.error(e.getMessage(),e);
      throw e;
    }
  }

  private void removeCurrentPatient() {
    this.currentPatientVM = PatientVM.createEmptyPatientFx();
  }

  public PatientVM getCurrentPatientVM() {
    return currentPatientVM;
  }

  public void selectPatientForEdit(PatientVM patientVM) {
    this.currentPatientVM = patientVM;
  }

  public void initializePeselTextField(String peselCode) {
    this.currentPatientVM.getPesel().set(peselCode);
  }

  BooleanProperty addPatientModeDisableProperty() {
    return addPatientModeDisable;
  }

  BooleanProperty isEditPatientModeProperty() {
    return isEditPatientMode;
  }

  void turnOnAddPatientMode() {
    this.dataEntryMode = PatientRegisterDataEntryMode.ADD;
    this.addPatientModeDisable.set(false);
    turnOnListenersForPreventingUnsavedChanges();
  }

  public void turnOnListenersForPreventingUnsavedChanges() {
    currentPatientVM
        .getName()
        .addListener( nameListener);
    currentPatientVM
        .getLastName()
        .addListener(lastNameListener);
  }

  void turnOnEditingPatientMode() {
    this.dataEntryMode = PatientRegisterDataEntryMode.EDIT;
    this.isEditPatientMode.set(false);
    this.disableCitizenCodeField.set( true );
  }

  public PatientRegisterDataEntryMode dataEntryMode() {
    return dataEntryMode;
  }
  public BooleanProperty disableCitizenCodeFieldProperty() {
    return disableCitizenCodeField;
  }

  public void turnOffListenersForPreventingUnsavedChanges() {
    currentPatientVM
            .getName()
            .removeListener( nameListener);
    currentPatientVM
            .getLastName()
            .removeListener(lastNameListener);
  }

  public Optional<PatientVM> commitCurrentPatientChanges() throws ValidationExceptions {
    logger.trace("[VM] Updating patient...");
    PatientDto patientToRegister = PatientDtoVmMapper.toDto( currentPatientVM );
    try {
      Optional<PatientDto> createdPatient = patientService.updatePatient(patientToRegister);
      if (createdPatient.isPresent()) {
        PatientVM fx = PatientDtoVmMapper.toVm(createdPatient.get());
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

  public void clearFields() {
    logger.debug( "[VM] Clear temp patient variable values..." );
    this.currentPatientVM = PatientVM.createEmptyPatientFx();
  }

  enum PatientRegisterDataEntryMode {
    READONLY,
    ADD,
    EDIT
  }
}
