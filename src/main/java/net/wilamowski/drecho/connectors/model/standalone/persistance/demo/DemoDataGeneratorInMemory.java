package net.wilamowski.drecho.connectors.model.standalone.persistance.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitEntity;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DemoDataGeneratorInMemory {
  private static final Logger logger = LogManager.getLogger( DemoDataGeneratorInMemory.class);
  private Random random;
  private String[] LAST_NAMES;
  private String[] FIRST_NAMES;
  private String[] CITIES;

  private DemoDataGeneratorInMemory() {
    logger.warn("[REPOSITORY] Initializing _DEMO_ data generator...");
    random = new Random();
    initializeCities();
    initializeNames();
    initializeLastNames();
  }

  public static DemoDataGeneratorInMemory instance() {
    return new DemoDataGeneratorInMemory( );
  }

  private void initializeCities() {
    var values = Lang.getString("dummy.comma_separated.cities_names");
    CITIES = values.split(",");
  }

  private void initializeNames() {
    var values = Lang.getString("dummy.comma_separated.firstnames");
    FIRST_NAMES = values.split(",");
  }

  private void initializeLastNames() {
    var values = Lang.getString("dummy.comma_separated.lastnames");
    LAST_NAMES = values.split(",");
  }
  public Patient patient() {
    LocalDate dateOfBirth = dateOfBirth();
    return Patient.builder()
            .name(name())
            .lastName(lastName())
            .pesel(newPesel(dateOfBirth))
            .nameOfCityBirth( cities())
            .codeOfCityBirth(getCodeOfCity())
            .dateBirth(dateOfBirth)
            .generalPatientNote(shortSentence())
            .patientTelephoneNumber(telephoneNumber())
            .build();
  }

  public String name() {
    return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
  }

  public String lastName() {
    return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
  }

  public String cities() {
    return CITIES[random.nextInt(CITIES.length)];
  }

  public LocalDate dateOfBirth() {
    return LocalDate.now().minusYears(random.nextInt(100) + 1).minusDays(random.nextInt(364) + 1);
  }

  public String getCodeOfCity() {
    return "12-123";
  }

  public String telephoneNumber() {
    return String.valueOf(random.nextInt(8) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1)
        + (random.nextInt( 8 ) + 1);
  }

  public String newPesel(LocalDate dateOfBirth) { //todo internatiozation, national citized id factory etc...
    String dateString = dateOfBirth.format(DateTimeFormatter.ofPattern("yyMMdd"));
    int    randomNumber          = random.nextInt( 88888 ) + 10000 ;
    return String.format("%s%d", dateString, randomNumber);
  }

  public String shortSentence() {
    return Lang.getString( "dummy.sentence.short" );
  }

  public Set<VisitEntity> loadDemoVisits() {
    Set<VisitEntity> visits = new HashSet<>();
    visits.add(
            new VisitEntity(
                    "ADM",
                    "ADM",
                    LocalDateTime.now().minusDays(5),
                    LocalDateTime.now().minusHours(5).minusMinutes(5),
                    1L));
    visits.add(
            new VisitEntity(
                    "ADM",
                    "ADM",
                    LocalDateTime.now().minusDays(4),
                    LocalDateTime.now().minusHours(4).minusMinutes(4),
                    2L));
    visits.add(
            new VisitEntity(
                    "ADM",
                    "ADM",
                    LocalDateTime.now().minusDays(3),
                    LocalDateTime.now().minusHours(3).minusMinutes(3),
                    3L));
    visits.add(
            new VisitEntity(
                    "ADM",
                    "ADM",
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusHours(2).minusMinutes(2),
                    4L));
    visits.add(
            new VisitEntity(
                    "ADM",
                    "ADM",
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().minusHours(1).minusMinutes(1),
                    5L));
    return visits;
  }
}
