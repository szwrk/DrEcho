package net.wilamowski.drecho.client.presentation.patients;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModels;
import net.wilamowski.drecho.client.application.infra.ViewModelsInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class PatientRegisterController
    implements ViewModelsInitializer, ViewHandlerInitializer, PostInitializable, Initializable {
  private static final Logger logger = LogManager.getLogger(PatientRegisterController.class);

  @FXML private TextField caregiverNoteTextField;

  @FXML private TextField caregiverTelephoneTextField;

  @FXML private TextField caregiverTelephoneTextField1;

  @FXML private TextField addressCodeOfCityTextField;

  @FXML private DatePicker patientDateBirthTextField;

  @FXML private TextArea patientGeneralPatientNote;

  @FXML private TextField patientIdTextField;

  @FXML private TextField patientLastNameTextField;

  @FXML private TextField addressNameOfCityTextField;

  @FXML private TextField patientNameTextField;

  @FXML private TextField patientStreetTextField;

  @FXML private TextField patientTelephoneNumberTextField;

  @FXML private TextField patientPeselTextField;

  @FXML private TextField patientCityOfBirthTextField;

  @FXML private TitledPane rootTitledPane;

  @FXML private Button updatePatientButton;
  @FXML private Button savePatientButton;
  @FXML private Button closeButton;

  private GeneralViewHandler viewHandler;
  private PatientRegisterViewModel viewModel;
  private ResourceBundle bundle;

  public void onActionSavePatient(ActionEvent event) {
    logger.debug("Click on save new patient...");

    Optional<PatientFx> savedPatient = Optional.empty();
    try {
      savedPatient = viewModel.commitCurrentPatientChanges();
    } catch (RuntimeException e) {
      logger.error("[CONTROLLER-PATIENT] An error occurred:", e.getMessage());
      String header = bundle.getString("e.012.header");
      String msg = e.getMessage();
      ExceptionAlert.create().showError(e, header, msg);
    }

    //    if (savedPatient.isPresent()) {
    //      handleAddNewPatient(savedPatient);
    //    } else {
    //      handleNullPatient();
    //    }
  }

  public void setTitle(String text) {
    this.rootTitledPane.setText(text);
  }

  void blockFields() {
    caregiverNoteTextField.setDisable(true);
    caregiverTelephoneTextField.setDisable(true);
    caregiverTelephoneTextField1.setDisable(true);
    addressCodeOfCityTextField.setDisable(true);
    patientDateBirthTextField.setDisable(true);
    patientGeneralPatientNote.setDisable(true);
    patientIdTextField.setDisable(true);
    patientLastNameTextField.setDisable(true);
    addressNameOfCityTextField.setDisable(true);
    patientNameTextField.setDisable(true);
    patientStreetTextField.setDisable(true);
    patientTelephoneNumberTextField.setDisable(true);
    patientPeselTextField.setDisable(true);
    patientCityOfBirthTextField.setDisable(true);
  }

  void unblockFields() {
    caregiverNoteTextField.setDisable(false);
    caregiverTelephoneTextField.setDisable(false);
    caregiverTelephoneTextField1.setDisable(false);
    addressCodeOfCityTextField.setDisable(false);
    patientDateBirthTextField.setDisable(false);
    patientGeneralPatientNote.setDisable(false);
    patientIdTextField.setDisable(false);
    patientLastNameTextField.setDisable(false);
    addressNameOfCityTextField.setDisable(false);
    patientNameTextField.setDisable(false);
    patientStreetTextField.setDisable(false);
    patientTelephoneNumberTextField.setDisable(false);
    patientPeselTextField.setDisable(false);
    patientCityOfBirthTextField.setDisable(false);
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public void postInitialize() {
    viewModelReBindings();
    requestFocus();
    Bindings.bindBidirectional(
        savePatientButton.disableProperty(), viewModel.addPatientModeDisableProperty());
    Bindings.bindBidirectional(
        updatePatientButton.disableProperty(), viewModel.updatePatientModeDisableProperty());
  }

  public void viewModelReBindings() {
    Bindings.bindBidirectional(
        Objects.requireNonNull(patientIdTextField.textProperty()),
        Objects.requireNonNull(viewModel.getCurrentPatientFx().getId()),
        new NumberStringConverter());
    Bindings.bindBidirectional(
        patientNameTextField.textProperty(), viewModel.getCurrentPatientFx().getName());
    Bindings.bindBidirectional(
        patientLastNameTextField.textProperty(), viewModel.getCurrentPatientFx().getLastName());
    Bindings.bindBidirectional(
        patientPeselTextField.textProperty(), viewModel.getCurrentPatientFx().getPesel());
    Bindings.bindBidirectional(
        patientDateBirthTextField.valueProperty(), viewModel.getCurrentPatientFx().getDateBirth());
    Bindings.bindBidirectional(
        patientCityOfBirthTextField.textProperty(),
        viewModel.getCurrentPatientFx().getCodeOfCityBirth());
    Bindings.bindBidirectional(
        patientGeneralPatientNote.textProperty(),
        viewModel.getCurrentPatientFx().getGeneralPatientNote());
    Bindings.bindBidirectional(
        patientTelephoneNumberTextField.textProperty(),
        viewModel.getCurrentPatientFx().getPatientTelephoneNumber());
  }

  private void requestFocus() {
    Platform.runLater(
        () -> {
          patientNameTextField.requestFocus();
        });
  }

  @Override
  public void initializeViewModels(ViewModels factory) {
    this.viewModel = factory.patientRegistrationViewModel();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.bundle = resourceBundle;
  }

  public PatientRegisterViewModel getViewModel() {
    return viewModel;
  }

  public void onActionEditPatient(ActionEvent event) {
    logger.debug("Clicked on the Edit button...");

    Optional<PatientFx> patientFxOptional = Optional.empty();
    try {
      patientFxOptional = viewModel.registerCurrentPatient();
    } catch (RuntimeException e) {
      logger.error("[CONTROLLER-PATIENT] An error occurred:", e.getMessage());
      String header = bundle.getString("e.012.header");
      String msg = e.getMessage();
      ExceptionAlert.create().showError(e, header, msg);
    }

    if (patientFxOptional.isPresent()) {
      handleAddNewPatient(patientFxOptional);
    } else {
      handleNullPatient();
    }
  }

  private void handleNullPatient() {
    logger.error("[CONTROLLER-PATIENT] Failed to save new patient.");
  }

  private void handleAddNewPatient(Optional<PatientFx> patientFxOptional) {
    PatientFx patientFx = patientFxOptional.get();
    UserAlert userAlert = new UserAlert();
    userAlert.showInfo(
        "New patient", "Patient added successfully: \n" + addedPatientInfo(patientFx));
    clearAllFields();
    logger.debug("[CONTROLLER-PATIENT] Added new patient: {}", patientFx);
  }

  private static String addedPatientInfo(PatientFx patientFx) {
    StringBuilder infoBuilder = new StringBuilder();
    Long id = patientFx.getId().getValue();
    String name = patientFx.getName().getValue();
    String lastName = patientFx.getLastName().getValue();
    String pesel = patientFx.getPesel().getValue();
    LocalDate dateOfBith = patientFx.getDateBirth().getValue();
    String formattedDate =
        dateOfBith == null ? " - " : dateOfBith.format(DateTimeFormatter.ISO_DATE);
    String birthCityName = patientFx.getNameOfCityBirth().getValue();
    String telephone = patientFx.getPatientTelephoneNumber().getValue();
    infoBuilder
        .append(id == null ? " - " : "\n" + id)
        .append(name == null ? " - " : "\n" + name)
        .append(lastName == null ? " - " : "\n" + lastName)
        .append(pesel == null ? " - " : "\n" + pesel)
        .append("\n" + formattedDate)
        .append(birthCityName == null ? " - " : "\n" + birthCityName)
        .append(telephone == null ? " - " : "\n" + telephone);
    return infoBuilder.toString();
  }

  private void clearAllFields() {
    caregiverNoteTextField.setText("");
    caregiverTelephoneTextField.setText("");
    caregiverTelephoneTextField1.setText("");
    addressCodeOfCityTextField.setText("");
    patientDateBirthTextField.setValue(null);
    patientGeneralPatientNote.setText("");
    patientIdTextField.setText("");
    patientLastNameTextField.setText("");
    addressNameOfCityTextField.setText("");
    patientNameTextField.setText("");
    patientStreetTextField.setText("");
    patientTelephoneNumberTextField.setText("");
    patientPeselTextField.setText("");
    patientCityOfBirthTextField.setText("");
  }

  public void onActionClosePatient(ActionEvent event) {
    logger.debug("Clicked on the Close button...");
    if (isReadOnlyModal()) {
      closeModal();
    } else {
      handleNonReadOnlyModal();
    }
  }

  private boolean isReadOnlyModal() {
    return viewModel
        .dataEntryMode()
        .equals(PatientRegisterViewModel.PatientRegisterDataEntryMode.READONLY);
  }

  private void handleNonReadOnlyModal() {
    if (viewModel.isUserStartedWriting()) {
      showUnsavedChangesAlert();
    } else {
      closeModal();
    }
  }

  private void showUnsavedChangesAlert() {
    UserAlert userAlert = new UserAlert();
    userAlert.showWarnConfirmOrLeave(
        "Unsaved Changes",
        "You have unsaved changes.\nDo you want to stop entering data?",
        "Continue adding",
        () -> logger.debug("Stay here"),
        "Abort",
        () -> closeModal());
  }

  private void closeModal() {
    Stage stage = (Stage) rootTitledPane.getScene().getWindow();
    stage.close();
  }
}
