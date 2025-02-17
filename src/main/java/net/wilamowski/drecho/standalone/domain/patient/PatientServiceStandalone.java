package net.wilamowski.drecho.standalone.domain.patient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.ToString;
import net.wilamowski.drecho.app.dto.PatientDto;
import net.wilamowski.drecho.configuration.backend_ports.PatientService;
import net.wilamowski.drecho.infra.connectors.mappers.PatientDomainDtoMapper;
import net.wilamowski.drecho.standalone.domain.patient.validations.Constraint;
import net.wilamowski.drecho.standalone.domain.patient.validations.ValidationExceptions;
import net.wilamowski.drecho.standalone.domain.patient.validations.Validator;
import net.wilamowski.drecho.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.standalone.persistance.VersionedPatientRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class PatientServiceStandalone implements PatientService {
  private static final Logger logger = LogManager.getLogger( PatientServiceStandalone.class);
  private final PatientRepository patientRepository;
  private final VersionedPatientRepository versionedPatientRepository;

  public PatientServiceStandalone(
      PatientRepository patientRepository, VersionedPatientRepository versionedPatientRepository) {
    this.patientRepository = patientRepository;
    this.versionedPatientRepository = versionedPatientRepository;
  }

  @Override
  public List<PatientDto> findByAny(String input) {
    logger.trace("[SERVICE] Entering findByAny...");
    logger.warn("Find by by any is not impl yet!");
    throw new NotImplementedException("Find by by any is not impl yet!");
  }

  @Override
  public List<PatientDto> findByPesel(String param, int page) {
    logger.trace("[SERVICE] Entering findByPesel...");
    return getPatients(param, page);
  }

  private List<PatientDto> getPatients(String param, int page) {
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
        List<Patient> byPeselCode = patientRepository.findByPeselCode( param , page );
        return byPeselCode.stream().map( PatientDomainDtoMapper::toDto ).collect( Collectors.toList());
      }
    }
    return Collections.emptyList();
  }

  @Override
  public List<PatientDto> findByFullName(String param, int page) {
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
      return byLastName.stream().map( PatientDomainDtoMapper::toDto ).collect( Collectors.toList());
    } else if (param.contains(" ")) {
      logger.debug("[SERVICE] Parameter contain white char. Returning all patient by full name");
      List<Patient> byLastName = patientRepository.findByFullName(param, page);
      logger.debug("[SERVICE] Founded patients number: {}", byLastName.size());
      return byLastName.stream().map( PatientDomainDtoMapper::toDto ).collect( Collectors.toList());
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
  public Optional<PatientDto> createPatientRecord(PatientDto patientDto) throws ValidationExceptions {
    logger.trace("[SERVICE] Entering createPatientRecord...");
    Patient      patient             = PatientDomainDtoMapper.toDomain( patientDto );
    Validator    newPatientValidator = validatePatientCreate( patient );
    List<String> errorList           = newPatientValidator.validateAll();

    if (errorList.isEmpty()) {
      Optional<Patient> optionalPatient = patientRepository.addNew( patient );
      if (optionalPatient.isPresent() ){
          return Optional.of( PatientDomainDtoMapper.toDto( patient ) );
      } else {
        return Optional.empty();
      }
    } else {
      logger.debug("[SERVICE-PATIENT] Creating patient... FAILED");
      String errorsMsg = "- " + String.join("\n - ", errorList);
      throw new ValidationExceptions(errorsMsg);
    }
  }

  private Validator validatePatientCreate(Patient patient) {
    logger.trace("[SERVICE-PATIENT] Entering validateNewPatient...");
    Validator newPatientValidator = Validator.instance();
    newPatientValidator.registerValidations( Constraint.nameNotNullConstraint( patient ));
    newPatientValidator.registerValidations(Constraint.lastNameNotNullConstraint( patient ));
    newPatientValidator.registerValidations(Constraint.peselNotNullConstraint( patient.getPesel()));
    newPatientValidator.registerValidations(
        Constraint.peselCodeLengthConstraint( patient.getPesel()));
    newPatientValidator.registerValidations(Constraint.patientPeselUniqueConstraint(this, patient ));
    logger.trace("[SERVICE-PATIENT] Exiting validateNewPatient method");
    return newPatientValidator;
  }

  private Validator validatPatientUpdate(Patient patient) {
    logger.trace("[SERVICE-PATIENT] Entering validateNewPatient...");
    Validator newPatientValidator = Validator.instance();
    newPatientValidator.registerValidations(Constraint.nameNotNullConstraint( patient ));
    newPatientValidator.registerValidations(Constraint.lastNameNotNullConstraint( patient ));
    newPatientValidator.registerValidations(Constraint.peselNotNullConstraint( patient.getPesel()));
    newPatientValidator.registerValidations(
            Constraint.peselCodeLengthConstraint( patient.getPesel()));
    logger.trace("[SERVICE-PATIENT] Exiting validateNewPatient method");
    return newPatientValidator;
  }

  @Override
  public Optional<PatientDto> updatePatient(PatientDto patientDto) throws ValidationExceptions {
    logger.trace("[SERVICE-PATIENT] Entering updatePatient...");
    Patient   patient              = PatientDomainDtoMapper.toDomain( patientDto );
    Validator newPatientValidator  = validatPatientUpdate( patient );
    List<String> errorList = newPatientValidator.validateAll();
    if (errorList.isEmpty()) {
      logger.debug("[SERVICE] Updating patient... SUCCESS");
      Optional<Patient> fetchedPatient = patientRepository.update( patient );
      if (fetchedPatient.isPresent()){
        PatientDto newPatientDto = PatientDomainDtoMapper.toDto( fetchedPatient.get( ) );
        return Optional.of( newPatientDto );
      } else {
        logger.warn( patient );
      }
      return Optional.empty();
    } else {
      logger.debug("[SERVICE-PATIENT] Updating patient... FAILED");
      String errorsMsg = "- " + String.join("\n - ", errorList);
      throw new ValidationExceptions(errorsMsg);
    }
  }

  @Override
  public Optional<Patient> findById(Long patientId) {
    return patientRepository.findById( patientId );
  }

  @Override
  public List<PatientDto> findRecentlyPatients() {
    int page = 1;
    logger.trace("[SERVICE] Entering findByFullName...");
    logger.debug("[SERVICE] Parameter  is alpha. Returning all patient by last name");
    List<Patient> byLastName = patientRepository.findRecent(page);
    logger.debug("[SERVICE] Founded patients number: {}", byLastName.size());
    return byLastName.stream().map( PatientDomainDtoMapper::toDto ).collect( Collectors.toList());

  }
}
