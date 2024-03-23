package net.wilamowski.drecho.connectors.model.standalone.domain.patient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.PatientService;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations.Constraint;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations.ValidationExceptions;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations.Validator;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.VersionedPatientRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class PatientStandaloneService implements PatientService {
  private static final Logger logger = LogManager.getLogger(PatientStandaloneService.class);
  private final PatientRepository patientRepository;
  private final VersionedPatientRepository versionedPatientRepository;

  public PatientStandaloneService(
      PatientRepository patientRepository, VersionedPatientRepository versionedPatientRepository) {
    this.patientRepository = patientRepository;
    this.versionedPatientRepository = versionedPatientRepository;
  }

  @Override
  public List<Patient> findByAny(String input) {
    logger.trace("[SERVICE] Entering findByAny...");
    logger.warn("Find by by any is not impl yet!");
    throw new NotImplementedException("Find by by any is not impl yet!");
  }

  @Override
  public List<Patient> findByPesel(String param, int page) {
    logger.trace("[SERVICE] Entering findByPesel...");
    return getPatients(param, page);
  }

  private List<Patient> getPatients(String param, int page) {
    logger.trace("[SERVICE] Entering getPatients...");
    logger.debug("Patient repository findBy input: {}", param.isBlank() ? "/blank/" : param);
    if (param == null) {
      IllegalArgumentException exception = new IllegalArgumentException("Search parameter is null");
      logger.error(exception);
    }

    if (param.isBlank()) {
      logger.debug("Searching parameter is null. Returning all patients");
    } else {
      if (StringUtils.isNumeric(param)) {
        logger.debug("Searching parametr is numeric. Returning all patient by pesel code");
        return patientRepository.findByPeselCode(param, page);
      }
    }
    return Collections.emptyList();
  }

  @Override
  public List<Patient> findByFullName(String param, int page) {
    logger.trace("[SERVICE] Entering findByFullName...");
    logger.debug(
        "[SERVICE] Patient repository findBy input: {}", param.isBlank() ? "/blank/" : param);

    if (param.isBlank()) {
      logger.debug("[SERVICE] Parameter is null. Returning all patients");
      return Collections.emptyList();
    }

    logger.debug("[SERVICE] Searching parameter is NOT null");
    if (StringUtils.isAlpha(param)) {
      logger.debug("[SERVICE] Parameter  is alpha. Returning all patient by last name");
      List<Patient> byLastName = patientRepository.findByFullName(param, page);
      logger.debug("[SERVICE] Founded patients number: {}", byLastName.size());
      return byLastName;
    } else if (param.contains(" ")) {
      logger.debug("[SERVICE] Parameter contain white char. Returning all patient by full name");
      List<Patient> byLastName = patientRepository.findByFullName(param, page);
      logger.debug("[SERVICE] Founded patients number: {}", byLastName.size());
      return byLastName;
    } else {
      logger.debug("[SERVICE] Parameter {} not handled. Return empty list.", param);
      return Collections.emptyList();
    }
  }

  @Override
  public int counterByFullName(String lastName) {
    logger.trace("[SERVICE] Entering counterByFullName...");
    return patientRepository.countByFullName(lastName);
  }

  @Override
  public Optional<Patient> createPatientRecord(Patient patient) throws ValidationExceptions {
    logger.trace("[SERVICE] Entering createPatientRecord...");
    Validator newPatientValidator = Validator.instance();
    newPatientValidator.registerValidations(Constraint.nameNotNullConstraint(patient));
    newPatientValidator.registerValidations(Constraint.lastNameNotNullConstraint(patient));
    newPatientValidator.registerValidations(Constraint.peselNotNullConstraint(patient.getPesel()));
    newPatientValidator.registerValidations(
        Constraint.peselCodeLengthConstraint(patient.getPesel()));
    newPatientValidator.registerValidations(Constraint.patientPeselUniqueConstraint(this, patient));
    List<String> errorList = newPatientValidator.validateAll();

    if (errorList.isEmpty()) {
      return patientRepository.addNew(patient);
    } else {
      logger.debug("[SERVICE-PATIENT] Creating patient... FAILED");
      String errorsMsg = "- " + String.join("\n - ", errorList);
      throw new ValidationExceptions(errorsMsg);
    }
  }

  @Override
  public Optional<Patient> updatePatient(Patient patient) throws ValidationExceptions {
    logger.trace("[SERVICE-PATIENT] Entering updatePatient...");
    Validator newPatientValidator = Validator.instance();
    newPatientValidator.registerValidations(Constraint.nameNotNullConstraint(patient));
    newPatientValidator.registerValidations(Constraint.lastNameNotNullConstraint(patient));
    newPatientValidator.registerValidations(Constraint.peselNotNullConstraint(patient.getPesel()));
    newPatientValidator.registerValidations(
        Constraint.peselCodeLengthConstraint(patient.getPesel()));
    newPatientValidator.registerValidations(Constraint.patientPeselUniqueConstraint(this, patient));
    List<String> errorList = newPatientValidator.validateAll();
    if (errorList.isEmpty()) {
      logger.debug("[SERVICE] Updating patient... SUCCESS");
      return patientRepository.update(patient);
    } else {
      logger.debug("[SERVICE-PATIENT] Updating patient... FAILED");
      String errorsMsg = "- " + String.join("\n - ", errorList);
      throw new ValidationExceptions(errorsMsg);
    }
  }
}
