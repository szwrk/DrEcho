package net.wilamowski.drecho.standalone.persistance.impl.inmemory;

import java.util.*;
import java.util.stream.Collectors;
import lombok.ToString;
import net.wilamowski.drecho.standalone.domain.patient.Patient;
import net.wilamowski.drecho.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.standalone.persistance.demo.DemoDataGeneratorInMemory;
import net.wilamowski.drecho.standalone.properties.BackendPropertyReader;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class PatientRepositoryInMemory implements PatientRepository {
  private static final Logger logger = LogManager.getLogger(PatientRepositoryInMemory.class);
  @ToString.Exclude private final List<Patient> patients = new ArrayList<>();
  private final DemoDataGeneratorInMemory dummyDataGeneratorInMemory;
  private Long patientId = 1L;

  private PatientRepositoryInMemory(DemoDataGeneratorInMemory demoDataGeneratorInMemory) {
    this.dummyDataGeneratorInMemory = demoDataGeneratorInMemory;
    Integer numberOfPatientsToGenerate = BackendPropertyReader.getInt("demo.patients-counter");
    initDemoPatients(numberOfPatientsToGenerate);
  }

  private void initDemoPatients(int numberOfPatient) {
    List<Patient> patients = dummyDataGeneratorInMemory.patients(numberOfPatient);
    patients.forEach(p -> addNew(p));
    logger.debug("Patients added to repository. Current repository size: {}", patients.size());

  }

  @Override
  public Optional<Patient> addNew(Patient patient) {
    logger.trace("[REPOSITORY] Entering addNew method");
    Long currentId = patientId++;
    Patient newPatient =
        Patient.builder()
            .id(currentId)
            .name(patient.getName())
            .pesel(patient.getPesel())
            .lastName(patient.getLastName())
            .nameOfCityBirth(patient.getNameOfCityBirth())
            .codeOfCityBirth(patient.getCodeOfCityBirth())
            .dateBirth(patient.getDateBirth())
            .generalPatientNote(patient.getGeneralPatientNote())
            .patientTelephoneNumber(patient.getPatientTelephoneNumber())
            .build();
    try {
      boolean added = patients.add(newPatient);
      if (!added) {
        logger.warn("[REPOSITORY] Failed to add patient to in-memory repository.");
        return Optional.empty();
      }
    } catch (UnsupportedOperationException e) {
      logger.error("[REPOSITORY] Add operation is not supported by this set.", e);
      return Optional.empty();
    } catch (ClassCastException e) {
      logger.error(
          "[REPOSITORY] Class of the specified element prevents it from being added to this set.",
          e);
      return Optional.empty();
    } catch (NullPointerException e) {
      logger.error(
          "[REPOSITORY] Specified element is null and this set does not permit null elements.", e);
      return Optional.empty();
    } catch (IllegalArgumentException e) {
      logger.error(
          "[REPOSITORY] Some property of the specified element prevents it from being added to this set.",
          e);
      return Optional.empty();
    }
    return Optional.of(newPatient);
  }

  @Override
  public Optional<Patient> findById(long id) {
    Optional<Patient> result =
        patients.stream().filter(patient -> patient.getId() == id).findFirst();
    logger.debug(
        "[REPOSITORY] Trying to find Patient with ID {} in the in-memory repository. Found: {}",
        id,
        result.orElse(null));
    return result;
  }

  @Override
  public List<Patient> findByFullName(String param, int pageNumber) {
    if ( param == null || param.isBlank( ) ) {
      logger.trace("[REPOSITORY] The passed parameter is empty, returning an empty list.");
      return Collections.emptyList();
    }
    logger.trace("[REPOSITORY] The passed parameter is not empty.");
    List<Patient> filteredPatients = filterByFullName(param);
    int pageSize =
        BackendPropertyReader.getInt("backend.patient.pagination.result-table-rows-on-page");
    int totalPatients = patients.size();
    int matchedPatients = filteredPatients.size();
    int startIndex = (pageNumber) * pageSize;
    int endIndex = startIndex + pageSize;
    logger.debug(
        "[REPOSITORY] Total Patients: "
            + totalPatients
            + " Matched Patients: "
            + matchedPatients
            + ", Start Index: "
            + startIndex
            + ", End Index: "
            + endIndex);
    if (filteredPatients.size() < pageSize) {
      return filteredPatients.subList(startIndex, filteredPatients.size());
    }
    return filteredPatients.subList(startIndex, endIndex);
  }

  @Override
  public int countByFullName(String param) {
    logger.trace( "[REPOSITORY] Entering countByLastName..." );
    logger.trace( "[REPOSITORY] Retriving number of record for pagination.... START" );
    int size = filterByFullName( param ).size( );
    logger.trace( "[REPOSITORY] Retriving number of record for pagination.... END" );
    logger.trace( "[REPOSITORY] Retriving results {}", size );
    return size;
  }

  @Override
  public List<Patient> findByPeselCode(String param, int page) {
    if (patients == null || param == null || param.isBlank()) {
      logger.debug("[REPOSITORY] Empty param or null patients list - returning empty list");
      return Collections.emptyList();
    }
    List<Patient> collect =
        patients.stream()
            .filter(patient -> patient.getPesel().startsWith(param))
            .collect(Collectors.toList());
    logger.debug("[REPOSITORY] Empty param - returning {} items}", collect.size());
    return collect;
  }

  public Optional<Patient> update(Patient updatedPatient) {
    Optional<Patient> existingPatientOptional =
        patients.stream().filter(p -> p.getId().equals(updatedPatient.getId())).findFirst();

    existingPatientOptional.ifPresent(
        existingPatient -> {
          Patient newPatient =
              Patient.builder()
                  .id(existingPatient.getId())
                  .name(updatedPatient.getName())
                  .pesel(updatedPatient.getPesel())
                  .lastName(updatedPatient.getLastName())
                  .nameOfCityBirth(updatedPatient.getNameOfCityBirth())
                  .codeOfCityBirth(updatedPatient.getCodeOfCityBirth())
                  .dateBirth(updatedPatient.getDateBirth())
                  .generalPatientNote(updatedPatient.getGeneralPatientNote())
                  .patientTelephoneNumber(updatedPatient.getPatientTelephoneNumber())
                  .build();

          patients.remove(existingPatient);
          patients.add(newPatient);
        });

    return existingPatientOptional;
  }

  @Override
  public List<Patient> findAll() {
    if (patients == null) {
      logger.warn("[REPOSITORY] Patient repository is null. Returning empty set...");
      return Collections.emptyList();
    }
    return patients;
  }

  private List<Patient> filterByFullName(String param) {
    logger.trace("[REPOSITORY] Entering filterByFullName");
    if (param == null) {
      logger.trace("[REPOSITORY] The passed parameter is empty, returning an empty list.");
      return Collections.emptyList();
    }
    logger.trace("[REPOSITORY] The passed parameter is NOT empty so analyze String...");
    String paramLowerCase = param.trim().toLowerCase();

    List<Patient> collect = new ArrayList<>();
    String[]      passedWords   = paramLowerCase.split( " " );
    int           passedWordCounter  = passedWords.length;
    try {
      if (passedWordCounter == 1){
        collect = handleOneWordProbablyLastName( paramLowerCase );
      } else if(passedWordCounter == 2){
        collect = handleOneWordProbablyFullName( passedWords );
      } else {
        logger.error("[REPOSITORY] . Passed words number: " + passedWords);
        throw new NotImplementedException("Passed name is more then two words: " + passedWords);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    logger.debug("[REPOSITORY] Returned patients: " + collect.size());
    return collect;
  }

  private List<Patient> handleOneWordProbablyFullName(String[] words) {
    logger.debug( "[REPOSITORY] Entering handleOneWordProbablyFullName" );
    String        lastName = words[0];
    String        firstName = words[1];
    logger.debug("[REPOSITORY] " + firstName + "-"+ lastName  );
    List<Patient> collect;
    collect =
            patients.stream()
                    .filter(patient -> patient.getLastName().toLowerCase().startsWith( lastName )
                            && patient.getName().toLowerCase().startsWith( firstName ) )
                    .collect(Collectors.toList());
    logger.trace("[REPOSITORY] Escaping with size: " + collect.size()  );
    return collect;
  }
  private List<Patient> handleOneWordProbablyLastName(String paramLowerCase) {
    logger.debug( "Repository handleOneWordProbablyLastName" );
    List<Patient> collect;
    collect =
        patients.stream()
            .filter(patient -> patient.getLastName().toLowerCase().startsWith( paramLowerCase ))
            .collect(Collectors.toList());
    return collect;
  }
  public static PatientRepositoryInMemory createPatientRepositoryInMemory(
      DemoDataGeneratorInMemory demoDataGeneratorInMemory) {
    return new PatientRepositoryInMemory(demoDataGeneratorInMemory);
  }
}
