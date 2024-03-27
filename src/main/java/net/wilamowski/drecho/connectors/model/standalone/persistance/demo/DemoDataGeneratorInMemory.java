package net.wilamowski.drecho.connectors.model.standalone.persistance.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author Arkadiusz Wilamowski
 */
public class DemoDataGeneratorInMemory {
  private static final Logger logger = LogManager.getLogger(DemoDataGeneratorInMemory.class);
  private final Random random;
  private String[] LAST_NAMES;
  private String[] FIRST_NAMES;
  private String[] CITIES;
  private List<Patient> patients = new ArrayList<>();
  private Long sequencePatient = 1L;

  private DemoDataGeneratorInMemory() {
    logger.warn("[REPOSITORY] Initializing _DEMO_ data generator...");
    random = new Random();
    initializeCities();
    initializeNames();
    initializeLastNames();
  }

  private void initializeCities() {
    var values = Lang.getString("demo.comma_separated.cities_names");
    if (values.isEmpty()) {
      String s = "The list of cities in the resource bundle is empty.";
      throw new IllegalArgumentException(s);
    }
    CITIES = values.split(",");
  }

  private void initializeNames() {
    var values = Lang.getString("demo.comma_separated.firstnames");
    if (values.isEmpty()) {
      String s = "The list of first names in the resource bundle is empty.";
      throw new IllegalArgumentException(s);
    }
    FIRST_NAMES = values.split(",");
  }

  private void initializeLastNames() {
    var values = Lang.getString("demo.comma_separated.lastnames");
    if (values.isEmpty()) {
      String s = "The list of last names in the resource bundle is empty.";
      throw new IllegalArgumentException(s);
    }
    LAST_NAMES = values.split(",");
  }

  public static DemoDataGeneratorInMemory instance() {
    return new DemoDataGeneratorInMemory();
  }

  public List<Patient> patients(int numberOfPatient) {
    patients = new ArrayList<>();
    int i = 1;
    while (i <= numberOfPatient) {
      patients.add(patient());
      i++;
    }
    logger.debug("[DEMO] Genereted demo patients:" + patients.size());
    return patients;
  }

  private Patient patient() {
    LocalDate dateOfBirth = dateOfBirth();
    return Patient.builder()
        .id(patientSequentNext())
        .name(name())
        .lastName(lastName())
        .pesel(randomCitizenId(dateOfBirth))
        .nameOfCityBirth(cities())
        .codeOfCityBirth(getCodeOfCity())
        .dateBirth(dateOfBirth)
        .generalPatientNote(shortSentence())
        .patientTelephoneNumber(telephoneNumber())
        .build();
  }

  private Long patientSequentNext() {
    return sequencePatient++;
  }

  private String name() {
    return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
  }

  private String lastName() {
    return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
  }

  private String cities() {
    return CITIES[random.nextInt(CITIES.length)];
  }

  private LocalDate dateOfBirth() {
    return LocalDate.now().minusYears(random.nextInt(100) + 1).minusDays(random.nextInt(364) + 1);
  }

  private String getCodeOfCity() {
    return "12-123"; // todo
  }

  private String telephoneNumber() {
    return String.valueOf(random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1)
        + (random.nextInt(8) + 1);
  }

  private String randomCitizenId(
      LocalDate dateOfBirth) { // todo internatiozation, national citized id factory etc...
    String dateString = dateOfBirth.format(DateTimeFormatter.ofPattern("yyMMdd"));
    int randomNumber = random.nextInt(88888) + 10000;
    return String.format("%s%d", dateString, randomNumber);
  }

  private String shortSentence() {
    return Lang.getString("demo.sentence.short");
  }

  public Set<Visit> generateVisitsForPatients() {
    final String operatorUserName = "ADM";
    Set<Visit> generatedVisits = new HashSet<>();
    if (patientsWasGenerated()) {
      genereteSomeVisitsForPatient(generatedVisits, operatorUserName);
      logger.debug("[DEMO] Created random visits. Amount: " + generatedVisits.size());
    } else {
      logger.error(
          "No patients found. Please ensure patients are generated before attempting to generate visits.");
      throw new IllegalStateException(
          "No patients found. Please ensure patients are generated before attempting to generate visits.");
    }
    return Collections.unmodifiableSet(generatedVisits);
  }

  private void genereteSomeVisitsForPatient(Set<Visit> visits, String userName) {
    patients.forEach(
        patient -> {
          User user = User.builder( ).login( "TEST1" ).firstName( "Jan" ).surname( "Kowalski" ).build( );

          int numberOfVisits = random.nextInt(5) + 1;
          while (numberOfVisits > 0) {
            visits.add(
                Visit.builder()
                    .selectedPerformer(user )
                    .selectedRegistrant(user)
                    .realizationDateTime(randomRealizationDateTime())
                    .viewStartDateTime(randomViewStartDateTimeProperty())
                    .patient( patient)
                    .build());
            numberOfVisits--;
          }
        });
  }

  private LocalDateTime randomRealizationDateTime() {
    int days = random.nextInt(366);
    int hours = random.nextInt(24);
    int minutes = random.nextInt(60);
    int seconds = random.nextInt(60);
    return LocalDateTime.now()
        .minusDays(days)
        .minusHours(hours)
        .minusMinutes(minutes)
        .minusSeconds(seconds);
  }

  private LocalDateTime randomViewStartDateTimeProperty() {
      int days = random.nextInt(366);
    int hours = random.nextInt(24);
    int minutes = random.nextInt(60);
    int seconds = random.nextInt(60);
    return LocalDateTime.now()
        .minusDays(days)
        .minusHours(hours)
        .minusMinutes(minutes)
        .minusSeconds(seconds);
  }

  private boolean patientsWasGenerated() {
    return !patients.isEmpty();
  }
}
