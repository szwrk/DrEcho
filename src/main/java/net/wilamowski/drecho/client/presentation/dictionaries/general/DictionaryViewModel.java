package net.wilamowski.drecho.client.presentation.dictionaries.general;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.DictionaryVmMapper;
import net.wilamowski.drecho.gateway.DictionariesService;
import net.wilamowski.drecho.standalone.domain.dictionary.Dictionary;
import net.wilamowski.drecho.standalone.domain.dictionary.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
@Getter(AccessLevel.PACKAGE)
public class DictionaryViewModel {
  private static final Logger logger = LogManager.getLogger(DictionaryViewModel.class);
  private final DictionariesService service;
  private final ListProperty<DictionaryFx> dictionariesFx =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ObjectProperty<DictionaryFx> selectedDictionary = new SimpleObjectProperty<>();
  private final BooleanProperty isDictionaryNowEdited = new SimpleBooleanProperty(false);
  private ChangeListener<String> positionCodeListener;
  private ChangeListener<String> positionNameListener;
  private ChangeListener<Boolean> positionActiveListener;

  public DictionaryViewModel(DictionariesService dictionariesService) {
    this.service = dictionariesService;
    initDictionary();
    createListeners();
  }

  private void createListeners() {
    positionNameListener =
        (observable, oldValue, newValue) -> {
          logger.debug(newValue);
        };
    positionCodeListener =
        (observable, oldValue, newValue) -> {
          logger.debug(newValue);
        };

    positionActiveListener =
        (observable, oldValue, newValue) -> {
          logger.debug(newValue);
        };
  }

  private void initDictionary() {
    DictionaryVmMapper dictionaryVmMapper = new DictionaryVmMapper();
    Optional<Set<Dictionary>> dictionaries = service.getDictionaries();
    List<DictionaryFx> collect =
        dictionaries.orElseThrow().stream()
            .map(dictionaryVmMapper::toJavaFx)
            .collect(Collectors.toList());
    dictionariesFx.get().setAll(collect);
  }

  public DictionaryFx getSelectedDictionary() {
    return selectedDictionary.get();
  }

  public ObjectProperty<DictionaryFx> selectedDictionaryProperty() {
    return selectedDictionary;
  }

  public void enable(Dictionary dictionary, Position positionRowValue) {
    service.disable(dictionary, positionRowValue);
  }

  public void disable(Dictionary dictionary, Position positionRowValue) {
    service.disable(dictionary, positionRowValue);
  }

  public ObservableList<DictionaryFx> getDictionariesFx() {
    return dictionariesFx.get();
  }

  public ListProperty<DictionaryFx> dictionariesFxProperty() {
    return dictionariesFx;
  }

  // listeners

  void addListeners(PositionFx positionFx) {
    positionFx.codeProperty().addListener(positionCodeListener);
    positionFx.nameProperty().addListener(positionNameListener);
    positionFx.activeProperty().addListener(positionActiveListener);
  }

  void removeListeners(PositionFx positionFx) {
    positionFx.codeProperty().removeListener(positionCodeListener);
    positionFx.nameProperty().removeListener(positionCodeListener);
    positionFx.activeProperty().removeListener(positionActiveListener);
  }

  public void saveChangesDictionary() {
    logger.debug("Saving selected dictionary changes");
    DictionaryFx selectedDictionaryFx = selectedDictionary.get();
    logger.debug("Selected dictionary: {}", selectedDictionaryFx);
    DictionaryVmMapper mapper = new DictionaryVmMapper();
    Dictionary dictionary = mapper.toDomain(selectedDictionaryFx);
    service.updateDictionary(dictionary);
  }

  public void chooseDictionary(DictionaryFx dictionaryFx) {
    logger.debug("Selected dictionary: {}", dictionaryFx);
    selectedDictionary.set(dictionaryFx);
  }

  public void enableDictionary() {
    this.isDictionaryNowEdited.set(true);
  }

  public void disableDictionary() {
    this.isDictionaryNowEdited.set(false);
  }
}
