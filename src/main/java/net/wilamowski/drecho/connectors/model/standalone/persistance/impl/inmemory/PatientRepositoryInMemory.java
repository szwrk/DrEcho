package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.util.*;
import java.util.stream.Collectors;
import lombok.ToString;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.demo.DemoDataGeneratorInMemory;
import net.wilamowski.drecho.connectors.properties.BackendPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class PatientRepositoryInMemory implements PatientRepository {
  private static final Logger logger = LogManager.getLogger(PatientRepositoryInMemory.class);

  private final List<Patient> patients = new ArrayList<>();
  private DemoDataGeneratorInMemory dummyDataGeneratorInMemory;
  private Long patientId = 1L;

  private PatientRepositoryInMemory() {
    Integer numberOfPatientsToGenerate  = BackendPropertyReader.getInt( "demo.patients-counter" );
    new Thread( () -> initDummyPatients(numberOfPatientsToGenerate ) ).start();
  }

  public static PatientRepositoryInMemory createPatientRepositoryInMemory( ) {
    return new PatientRepositoryInMemory(  );
  }

  private void initDummyPatients(int numberOfPatient) {
    dummyDataGeneratorInMemory = DemoDataGeneratorInMemory.instance();
    for (int i = 1; i <= numberOfPatient; i++) {
      Patient newPatient = dummyDataGeneratorInMemory.patient();
      this.addNew(newPatient);
    }
  }

  @Override
  public Optional<Patient> addNew(Patient patient) {
    logger.debug("[REPOSITORY] Creating patient");
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
    patient = null;
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
      logger.error("[REPOSITORY] Class of the specified element prevents it from being added to this set.", e);
      return Optional.empty();
    } catch (NullPointerException e) {
      logger.error("[REPOSITORY] Specified element is null and this set does not permit null elements.", e);
      return Optional.empty();
    } catch (IllegalArgumentException e) {
      logger.error(
          "[REPOSITORY] Some property of the specified element prevents it from being added to this set.", e);
      return Optional.empty();
    }
    logger.debug(
        "[REPOSITORY] Added new patient to in memory repository. Data: {}", newPatient);
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
  public List<Patient> findByLastName(String param, int pageNumber) {
    if (patients == null || param == null || param.isBlank()) {
      return Collections.emptyList();
    }
    List<Patient> filteredPatients =
            filterByLastName( param );
    int pageSize = BackendPropertyReader.getInt( "backend.patient.pagination.result-table-rows-on-page" );
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
    if (filteredPatients.size()<pageSize){
      return filteredPatients.subList(startIndex, filteredPatients.size());
    }
    return filteredPatients.subList(startIndex, endIndex);
  }
  @Override
  public int countByLastName(String param) {
    return filterByLastName( param ).size();
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
    Optional<Patient> existingPatientOptional = patients.stream()
            .filter(p -> p.getId().equals(updatedPatient.getId()))
            .findFirst();

    existingPatientOptional.ifPresent(existingPatient -> {
      Patient newPatient = Patient.builder()
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

  private List<Patient> filterByLastName(String param) {
    if (param==null){
      return Collections.emptyList();
    }
    String paramLowerCase = param.trim().toLowerCase();
    return patients.stream( )
            .filter( patient -> patient.getLastName( ).toLowerCase( ).startsWith( paramLowerCase ) )
            .collect( Collectors.toList( ) );
  }
}
