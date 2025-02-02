package net.wilamowski.drecho.client.presentation.patients;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.PatientDtoVmMapper;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.gateway.PatientService;
import net.wilamowski.drecho.app.dto.PatientDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class PatientSearcherViewModel {
  private static final Logger logger = LogManager.getLogger(PatientSearcherViewModel.class);
  private final int AUTOSEARCH_NUMBER_LENGTH_TRIGGER;
  private final int AUTOSEARCH_TEXT_LENGTH_TRIGGER;
  private final PatientService patientService;

  @ToString.Exclude
  private final ObservableList<PatientVM> patients = FXCollections.observableArrayList();

  @ToString.Exclude
  private final ObjectProperty<PatientVM> selectedPatient = new SimpleObjectProperty<>();

  private final SimpleStringProperty searchTextProperty = new SimpleStringProperty();

  public PatientSearcherViewModel(PatientService patientService) {
    this.patientService = patientService;
    AUTOSEARCH_NUMBER_LENGTH_TRIGGER =
        Integer.parseInt(
            ClientPropertyReader.getString(
                "admin.patient.searcher.autosearch-number-length-trigger"));
    AUTOSEARCH_TEXT_LENGTH_TRIGGER =
        Integer.parseInt(
            ClientPropertyReader.getString(
                "admin.patient.searcher.autosearch-text-length-trigger"));
  }

  public void setCurrentPatient(PatientVM patient) {
    Objects.requireNonNull(patient, "Selected patient is null");
    selectedPatient.set(patient);
  }

  public void unsetCurrentPatient() {
    selectedPatient.setValue( null );
  }

  public int searchPatientByFullName(String param) {
    logger.trace("[VM] Entering search method with param: {}", param);
    if (param == null) {
      logger.trace("[VM] Escaping search, param is null. Return 0");
      return 0;
    }

    String cleanedInput = param.trim().toLowerCase();;
    if ( looksLikeCitizenNumericCode( param , cleanedInput ) ) { //todo it's mvp, but it should be configurable some day
      logger.debug("[VM] Search patient input value qualified as numeric");
      return handleSearchByPesel(cleanedInput);
    } else if ( looksLikePatientName( param  ) ) {
      logger.debug("[VM] Search patient input value qualified as last name");
      return handleSearchByFullName(cleanedInput);
    } else if ( looksLikePatientFullName( param  ) ) {
      logger.debug("[VM] Search patient input value qualified as full name");
      int founded = handleSearchByFullName( cleanedInput );
      logger.debug("[VM] Search patient input value qualified as full name return {}",founded );
      return handleSearchByFullName(cleanedInput);
    } else {
      // consume
      logger.debug("[VM] Input does not meet criteria for start searching. Ignoring.");
      return 0;
    }
  }

  private boolean looksLikePatientFullName(String param) {
    return param.contains(" ");
  }

  private boolean looksLikePatientName(String param) {
    return StringUtils.isAlpha( param )
            && !param.contains( " " )
            && param.length( ) >= AUTOSEARCH_TEXT_LENGTH_TRIGGER;
  }

  private boolean looksLikeCitizenNumericCode(String param , String cleanedInput) {
    return StringUtils.isNumeric( cleanedInput ) && param.length( ) >= AUTOSEARCH_NUMBER_LENGTH_TRIGGER;
  }

  private int handleSearchByPesel(String searchInput) {
    logger.trace("[VM] Entering handle search by pesel code");
    List<PatientDto> fetchedPatients = patientService.findByPesel(searchInput, 0);
    if (fetchedPatients.size() == 1) {
      logger.debug("[VM] Service return values: {}", fetchedPatients.size());
      List<PatientVM> patientsFxBean = PatientDtoVmMapper.toListToFx(fetchedPatients);
      updatePatientsTable(patientsFxBean);
      chooseFirstPatientIfOnlyOneResult();
      return fetchedPatients.size();
    } else if (fetchedPatients.isEmpty()) {
      logger.debug("[VM] Service return values: {}", fetchedPatients.size());
      updatePatientsTable(Collections.emptyList());
      unsetCurrentPatient();
      return fetchedPatients.size();
    } else {
      logger.debug("[VM] Service return values: {}", fetchedPatients.size());
      List<PatientVM> retrievedPatientsFx = PatientDtoVmMapper.toListToFx(fetchedPatients);
      updatePatientsTable(retrievedPatientsFx);
      unsetCurrentPatient();
      return fetchedPatients.size();
    }
  }

  private int handleSearchByFullName(String searchInput) {
    logger.trace("Entering handle search by full name...");
    List<PatientDto> foundedPatients = patientService.findByFullName(searchInput, 0);
    int           fetchedPatientNumber            = foundedPatients.size( );
    if (fetchedPatientNumber==0) {
      logger.trace("Fetched patient: 0");
      List<PatientVM> patientsFxBean = PatientDtoVmMapper.toListToFx(foundedPatients);
      updatePatientsTable(patientsFxBean);
      unsetCurrentPatient();
      return fetchedPatientNumber;
    } else if ( fetchedPatientNumber == 1 ) {
      logger.trace("Fetched patient: 1");
      List<PatientVM> patientsFxBean = PatientDtoVmMapper.toListToFx(foundedPatients);
      updatePatientsTable(patientsFxBean);
      chooseFirstPatientIfOnlyOneResult();
      return fetchedPatientNumber;
    } else {
      logger.trace("Fetched patient: else");
      List<PatientVM> retrievedPatientsFx = PatientDtoVmMapper.toListToFx(foundedPatients);
      updatePatientsTable(retrievedPatientsFx);
      unsetCurrentPatient();
      return fetchedPatientNumber;
    }
  }

  public BooleanBinding isPatientNull(){
    return selectedPatientProperty().isNull();
  }
  
  private void chooseFirstPatientIfOnlyOneResult() {
    selectedPatientProperty().set(patients.get(0));
  }

  private void updatePatientsTable(List<PatientVM> patientsFxBean) {
    patients.clear();
    patients.addAll(patientsFxBean);
  }

  public ObservableList<PatientVM> getPatients() {
    return patients;
  }

  public ListProperty<PatientVM> patientsProperty() {
    return new SimpleListProperty<>(patients);
  }

  SimpleStringProperty searchTextProperty() {
    return searchTextProperty;
  }

  public PatientVM getSelectedPatient() {
    return selectedPatient.get();
  }

  public ObjectProperty<PatientVM> selectedPatientProperty() {
    return selectedPatient;
  }

  public void initializeSearchValue(String text) {}

  public int countPatientByAnyInput(String searchInput) {
    return patientService.counterByFullName(searchInput);
  }

  public Property<String> searchValueProperty() {
    return searchTextProperty;
  }
}
