package net.wilamowski.drecho.standalone.persistance.impl.inmemory;

import java.util.*;
import lombok.ToString;
import net.wilamowski.drecho.gateway.DictionariesService;
import net.wilamowski.drecho.standalone.domain.dictionary.Dictionary;
import net.wilamowski.drecho.standalone.domain.dictionary.DictionaryBuilder;
import net.wilamowski.drecho.standalone.domain.dictionary.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class DictionariesServiceInMemory implements DictionariesService {

  private static final Logger logger =
      LogManager.getLogger( DictionariesServiceInMemory.class);
  private final Set<net.wilamowski.drecho.standalone.domain.dictionary.Dictionary>
      repository;
  private final Map<String, String> dictionaries = new HashMap<>();

  public DictionariesServiceInMemory() {
    this.repository = new HashSet<>();
    loadEchoTteDictionaries();
    laodProvidersUsers();
    laodRegistranrsUsers();
    loadRealizationMinutes(5);
    loadRealizationHour();
    logger.debug(this);
  }

  private void loadRealizationHour() {
    createAndAddDictionary(
        "VST_REALIZ_HOURS",
        "Realization hours",
        " ",
        Position.of("12", "12", 12),
        Position.of("13", "13", 13),
        Position.of("14", "14", 14),
        Position.of("15", "15", 15),
        Position.of("16", "16", 16),
        Position.of("17", "17", 17),
        Position.of("18", "18", 18),
        Position.of("19", "19", 19),
        Position.of("20", "20", 20),
        Position.of("21", "21", 21),
            Position.of("22", "22", 22),
            Position.of("23", "23", 23)
    );
  }

  private void loadRealizationMinutes(int interval) {
    List<Position> values = new ArrayList<>();
    for (int i = 0; i < 60; i = i + interval) {
      if (i < 10) {
        var leadZero = "0";
        leadZero += String.valueOf(i);
        values.add(Position.of(leadZero, leadZero, i));
      } else {
        var s = String.valueOf(i);
        values.add(Position.of(s, s, i));
      }
    }
    createAndAddDictionary("VST_REALIZ_MIN", "Realization minutes", " ", values);
  }

  private void createAndAddDictionary(
      String code, String name, String description, List<Position> list) {
    DictionaryBuilder builder = new DictionaryBuilder();
    net.wilamowski.drecho.standalone.domain.dictionary.Dictionary dictionary =
        builder
            .setCode(code)
            .setName(name)
            .setDescription(description)
            .addPositions(list)
            .createDictionary();
    repository.add(dictionary);
  }

  private void laodProvidersUsers() {
    createAndAddDictionary(
        "PRSPERF",
        "Performers.",
        " ",
        Position.of("JK", "Kowalski Jan", 0),
        Position.of("SN", "Nowak Stefan", 1),
        Position.of("KC", "Czubaszek Krystyna", 2),
        Position.of("ADM", "Jan Kowalski", 3));
  }

  private void laodRegistranrsUsers() {
    createAndAddDictionary(
        "PRSREGI",
        "Registranrs.",
        " ",
        Position.of("JK", "Kowalski Jan", 0),
        Position.of("SN", "Nowak Stefan", 1),
        Position.of("KC", "Czubaszek Krystyna", 2),
        Position.of("ADM", "Jan Kowalski", 3));
  }

  private void loadEchoTteDictionaries() {
    mitralSection();
    aortalSection();
    triSection();
    pulmoSection();
    contractilitySection();
  }

  private void contractilitySection() {
    createAndAddDictionary(
        "OTCNTTXT",
        "Contractility description",
        "Dictionary description",
        Position.of("0", "OTCNTTXT", 0),
        Position.of("1", "Contractility text 1", 11),
        Position.of("2", "Contractility text 2", 12),
        Position.of("3", "Contractility text 3", 13));
  }

  private void pulmoSection() {
    createAndAddDictionary(
        "PVREGWAV",
        "Pulmo valve",
        "General regurgitation wave dictionary.",
        Position.of("0", "PVREGWAV", 0),
        Position.of("I", "I", 1),
        Position.of("II", "II", 2),
        Position.of("II/III", "II/III", 3),
        Position.of("III", "I/II", 4),
        Position.of("IV", "IV", 5),
        Position.of("NO", "nieobecny", 6),
        Position.of("SL", "śladowa", 7),
        Position.of("SL1", "ślad/I", 8),
        Position.of("TST", "śladowa", 9));

    createAndAddDictionary(
        "PVDSTFN",
        "Diastolic function. Pulmo valve.",
        "General regurgitation wave dictionary.",
        Position.of("1", "PVDSTFN Diastolic position 1", 11),
        Position.of("2", "PVDSTFN Diastolic position 2", 12),
        Position.of("3", "PVDSTFN Diastolic position 3", 13));

    createAndAddDictionary(
        "PVDESCTXT",
        "Pulmo valve. Ready to use description of this valve.",
        "General regurgitation wave dictionary.",
        Position.of("1", "PVDESCTXT Pulmo text 1", 11),
        Position.of("2", "PVDESCTXT Pulmo text 2", 12),
        Position.of("3", "PVDESCTXT Pulmo text 3", 13));
  }

  private void triSection() {
    createAndAddDictionary(
        "TVREGWAV",
        "Tri valve",
        "General regurgitation wave dictionary.",
        Position.of("0", "TVREGWAV", 0),
        Position.of("I", "I", 1),
        Position.of("II", "II", 2),
        Position.of("II/III", "II/III", 3),
        Position.of("III", "I/II", 4),
        Position.of("IV", "IV", 5),
        Position.of("NO", "nieobecny", 6),
        Position.of("SL", "śladowa", 7),
        Position.of("SL1", "ślad/I", 8));

    createAndAddDictionary(
        "TVDESCTXT",
        "Tricuspid valve. Ready to use description of this valve.",
        "General regurgitation wave dictionary.",
        Position.of("0", "TVDESCTXT", 0),
        Position.of("1", "Mitral text 1", 11),
        Position.of("2", "Mitral text 2", 12),
        Position.of("3", "Mitral text 3", 13));
  }

  private void aortalSection() {
    createAndAddDictionary(
        "AVREGWAV",
        "Aortal valve",
        "General regurgitation wave dictionary.",
        Position.of("0", "AVREGWAV", 0),
        Position.of("I", "I", 1),
        Position.of("II", "II", 2),
        Position.of("II/III", "II/III", 3),
        Position.of("III", "I/II", 4),
        Position.of("IV", "IV", 5),
        Position.of("NO", "nieobecny", 6),
        Position.of("SL", "śladowa", 7),
        Position.of("SL1", "ślad/I", 8));

    createAndAddDictionary(
        "AVDESCTXT",
        "Aortal valve. Ready to use description of this valve.",
        "General regurgitation wave dictionary.",
        Position.of("0", "AVDESCTXT", 0),
        Position.of("1", "Aortal text 1", 11),
        Position.of("2", "Aortal text 2", 12),
        Position.of("3", "Aortal text 3", 13));
  }

  private void mitralSection() {
    createAndAddDictionary(
        "MVREGWAV",
        "Mitral valve",
        "General regurgitation wave dictionary.",
        Position.of("0", "MVREGWAV", 0),
        Position.of("I", "I", 1),
        Position.of("II", "II", 2),
        Position.of("II/III", "II/III", 3),
        Position.of("III", "I/II", 4),
        Position.of("IV", "IV", 5),
        Position.of("NO", "nieobecny", 6),
        Position.of("SL", "śladowa", 7),
        Position.of("SL1", "ślad/I", 8));
    createAndAddDictionary(
        "MVDESCTXT",
        "Mitral valve. Ready to use description of this valve.",
        "General regurgitation wave dictionary.",
        Position.of("0", "MVDESCTXT", 0),
        Position.of("1", "Mitral text 1", 11),
        Position.of("2", "Mitral text 2", 12),
        Position.of("3", "Mitral text 3", 13));
  }

  private void createAndAddDictionary(
      String code, String name, String description, Position... positions) {
    DictionaryBuilder builder = new DictionaryBuilder();
    net.wilamowski.drecho.standalone.domain.dictionary.Dictionary dictionary =
        builder
            .setCode(code)
            .setName(name)
            .setDescription(description)
            .addPositions(Arrays.asList(positions))
            .createDictionary();
    repository.add(dictionary);
  }

  @Override
  public void addDictionary(
      net.wilamowski.drecho.standalone.domain.dictionary.Dictionary dictionary) {
    logger.debug(dictionary);
    repository.add(dictionary);
  }

  public Optional<net.wilamowski.drecho.standalone.domain.dictionary.Dictionary>
      getDictionary(String dictCode) {
    return getFirst(dictCode);
  }

  public Optional<
          Set<net.wilamowski.drecho.standalone.domain.dictionary.Dictionary>>
      getDictionaries() {
    return Optional.of(repository);
  }

  @Override
  public void enable(
      net.wilamowski.drecho.standalone.domain.dictionary.Dictionary dictionary,
      Position positionRowValue) {
    logger.warn("[REPOSITORY] not impl yet");
  }

  @Override
  public void disable(
      net.wilamowski.drecho.standalone.domain.dictionary.Dictionary dictionary,
      Position positionRowValue) {
    logger.warn("[REPOSITORY] not impl yet");
  }

  @Override
  public void updateDictionary(
      net.wilamowski.drecho.standalone.domain.dictionary.Dictionary dictionary) {
    logger.info("[REPOSITORY] Executing updateDictionary service method.");
    logger.debug("[REPOSITORY] Update dictionary. Input value: {}", dictionary);
    Optional<net.wilamowski.drecho.standalone.domain.dictionary.Dictionary>
        optionalDictionary =
            repository.stream().filter(d -> dictionary.getCode().equals(d.getCode())).findFirst();

    optionalDictionary.ifPresent(
        existingDictionary -> {
          logger.debug("[REPOSITORY] Found dictionary: {}", existingDictionary);
          DictionaryBuilder dictionaryBuilder = new DictionaryBuilder();
          net.wilamowski.drecho.standalone.domain.dictionary.Dictionary
              newDictionary = dictionaryBuilder.create(dictionary);

          logger.debug("[REPOSITORY] Remove {}", existingDictionary);
          repository.remove(existingDictionary);

          logger.debug("[REPOSITORY] Add {}", newDictionary);
          repository.add(newDictionary);
        });
  }

  private Optional<Dictionary> getFirst(String dictCode) {
    return repository.stream()
        .filter(dictionary -> dictionary.getCode().equals(dictCode))
        .findFirst();
  }
}
