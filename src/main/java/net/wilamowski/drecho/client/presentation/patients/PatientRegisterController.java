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
import net.wilamowski.drecho.app.bundle.Lang;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserAlert;
import net.wilamowski.drecho.client.presentation.customs.modals.UserDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class PatientRegisterController
    implements Initializable {
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

  public PatientRegisterController(GeneralViewHandler viewHandler , PatientRegisterViewModel viewModel) {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
  }

  public void onActionAddNewPatient(ActionEvent event) {
    logger.debug("Click on save new patient...");
    Optional<PatientVM> newPatient = Optional.empty();
    try {
      newPatient = viewModel.registerNewPatient();
    } catch (RuntimeException e) {
      logger.error("[CONTROLLER-PATIENT] An error occurred: {}\n{}", e.getMessage(), e);
      String header = Lang.getString("e.012.header");
      String msg = e.getMessage();
      ExceptionAlert.create().showError(e, header, msg);
    }

    if (newPatient.isPresent()) {
      PatientVM patient = newPatient.get();
      UserAlert.simpleInfo(
              "Successfully",
              "Added patient "
                  + patient.getLastName()
                  + " "
                  + patient.getName()
                  + " "
                  + patient.getDateBirth())
          // todo add details responseInfo(patient)
          .showAndWait();

    } else {
      UserAlert.simpleInfo("Failed", "Patient is empty");
    }
  }

  public void setTitle(String text) {
    this.rootTitledPane.setText(text);
  }

  public void blockFields() {
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

  private void requestFocus() {
    Platform.runLater(
        () -> {
          patientNameTextField.requestFocus();
        });
  }



  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    PatientVM currentPatientVM = viewModel.getCurrentPatientVM();

    Bindings.bindBidirectional(
            Objects.requireNonNull(patientIdTextField.textProperty()),
            Objects.requireNonNull(currentPatientVM.getId()),
            new NumberStringConverter());
    Bindings.bindBidirectional(patientNameTextField.textProperty(), currentPatientVM.getName());
    Bindings.bindBidirectional(
            patientLastNameTextField.textProperty(), currentPatientVM.getLastName());
    Bindings.bindBidirectional(patientPeselTextField.textProperty(), currentPatientVM.getPesel());
    Bindings.bindBidirectional(
            patientDateBirthTextField.valueProperty(), currentPatientVM.getDateBirth());
    Bindings.bindBidirectional(
            patientCityOfBirthTextField.textProperty(), currentPatientVM.getCodeOfCityBirth());
    Bindings.bindBidirectional(
            patientGeneralPatientNote.textProperty(), currentPatientVM.getGeneralPatientNote());
    Bindings.bindBidirectional(
            patientTelephoneNumberTextField.textProperty(),
            currentPatientVM.getPatientTelephoneNumber());
    //
    requestFocus();
    Bindings.bindBidirectional(
            savePatientButton.disableProperty(), viewModel.addPatientModeDisableProperty());
    Bindings.bindBidirectional(
            updatePatientButton.disableProperty(), viewModel.isEditPatientModeProperty());
    Bindings.bindBidirectional(
            patientPeselTextField.disableProperty(), viewModel.disableCitizenCodeFieldProperty());
  }

  public PatientRegisterViewModel getViewModel() {
    return viewModel;
  }



  public void onActionEditPatient(ActionEvent event) {
    logger.debug("Clicked on the Edit button...");

    Optional<PatientVM> patientFxOptional = Optional.empty();
    try {
      patientFxOptional = viewModel.commitCurrentPatientChanges();
    } catch (RuntimeException e) {
      logger.error("[CONTROLLER-PATIENT] An error occurred:", e.getMessage());
      String header = Lang.getString("e.012.header");
      String msg = e.getMessage();
      ExceptionAlert.create().showError(e, header, msg);
    }

    if (patientFxOptional.isPresent()) {
      handleSuccessAddNewPatient(patientFxOptional.get());
    } else {
      handleNullPatient();
    }
  }

  private void handleNullPatient() {
    logger.error("[CONTROLLER-PATIENT] Failed to save new patient.");
  }

  private void handleSuccessAddNewPatient(PatientVM patient) {
    UserAlert.simpleInfo(
                   "Added patient:",
           "\n" + responseInfo(patient)) //todo vm? imho persistance layer user!
        .showAndWait();
    logger.debug("[CONTROLLER-PATIENT] Added new patient: {}", patient);
  }

  private static String responseInfo(PatientVM patientVM) {
    StringBuilder infoBuilder = new StringBuilder();
    Long id = patientVM.getId().getValue();
    String name = patientVM.getName().getValue();
    String lastName = patientVM.getLastName().getValue();
    String pesel = patientVM.getPesel().getValue();
    LocalDate dateOfBith = patientVM.getDateBirth().getValue();
    String formattedDate =
        dateOfBith == null ? " - " : dateOfBith.format(DateTimeFormatter.ISO_DATE);
    String birthCityName = patientVM.getNameOfCityBirth().getValue();
    String telephone = patientVM.getPatientTelephoneNumber().getValue();
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

  private void clearControllerFields() {
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
    UserDialog dialog = null;
    UserDialog finalDialog = dialog;
    dialog = UserDialog.builder( )
            .title( "Information" )
            .header( "Unsaved Changes" )
            .content( "You have unsaved changes.\nDiscard changes?" )
            .addButton( "No, stay here" , () -> finalDialog.close())
            .addButton( "Yes, abort all" , () -> closeModal( ) )
            .build( );
     dialog.showAndWait();
  }

  private void closeModal() {
    Stage stage = (Stage) rootTitledPane.getScene().getWindow();
    Stage ownerStage = (Stage) stage.getOwner();
    if (ownerStage != null) {
      GeneralViewHandler.disableBlur(ownerStage);
    }
    stage.close();
  }


  public void bindControlsWithCurrentPatient() { //todo
    PatientVM currentPatientVM = viewModel.getCurrentPatientVM();

    Bindings.bindBidirectional(
            Objects.requireNonNull(patientIdTextField.textProperty()),
            Objects.requireNonNull(currentPatientVM.getId()),
            new NumberStringConverter());
    Bindings.bindBidirectional(patientNameTextField.textProperty(), currentPatientVM.getName());
    Bindings.bindBidirectional(
            patientLastNameTextField.textProperty(), currentPatientVM.getLastName());
    Bindings.bindBidirectional(patientPeselTextField.textProperty(), currentPatientVM.getPesel());
    Bindings.bindBidirectional(
            patientDateBirthTextField.valueProperty(), currentPatientVM.getDateBirth());
    Bindings.bindBidirectional(
            patientCityOfBirthTextField.textProperty(), currentPatientVM.getCodeOfCityBirth());
    Bindings.bindBidirectional(
            patientGeneralPatientNote.textProperty(), currentPatientVM.getGeneralPatientNote());
    Bindings.bindBidirectional(
            patientTelephoneNumberTextField.textProperty(),
            currentPatientVM.getPatientTelephoneNumber());
  }
}
