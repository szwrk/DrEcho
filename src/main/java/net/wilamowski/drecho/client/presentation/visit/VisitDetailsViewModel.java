package net.wilamowski.drecho.client.presentation.visit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.UserVmMapper;
import net.wilamowski.drecho.client.presentation.dictionaries.general.ListLoader;
import net.wilamowski.drecho.client.presentation.dictionaries.general.PositionFx;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.connectors.model.SimpleDictionariesService;
import net.wilamowski.drecho.connectors.model.VisitModel;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.UserService;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.shared.auth.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
@ToString
@Getter
public class VisitDetailsViewModel {
    private static final Logger logger = LogManager.getLogger( VisitDetailsViewModel.class);
    private final ObjectProperty<PatientFx> selectedPatient = new SimpleObjectProperty<>();
    private final UserService userService;
    private final ObjectProperty<UserVM> selectedRegistrant = new SimpleObjectProperty<>();
    private final ObjectProperty<UserVM> selectedPerformer = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> viewStartDtProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> viewStartTimeProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> realizationDtProperty = new SimpleObjectProperty<>();
    private final ListProperty<PositionFx> realizationHoursValues = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<PositionFx> realizationMinutesValues = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ResourceBundle bundle = null;
    private VisitModel service = null;
    private SimpleDictionariesService dictService = null;
    private final ListProperty<PositionFx> registrantValues =
            new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<PositionFx> performerValues =
            new SimpleListProperty<>(FXCollections.observableArrayList());


    public VisitDetailsViewModel(VisitModel service,  SimpleDictionariesService dictService, UserService userService) {
        this.service = service;
        this.dictService = dictService;
        this.userService = userService;
        initStartDateWithNow();
        initStartTimeWithNow();
        loadDict();
        ListLoader.source(dictService).loadValuesToFxList("VST_REALIZ_HOURS", realizationHoursValues);
        ListLoader.source(dictService).loadValuesToFxList("VST_REALIZ_MIN", realizationMinutesValues);
    }

    private void initStartTimeWithNow() {
        Boolean initVisitRealizationTime = Boolean.valueOf( ClientPropertyReader.getString( "user.ui.quick-visit-view.initialize-realization-time" ));
        if (initVisitRealizationTime) {
            viewStartTimeProperty.set(LocalTime.now());
        }
    }

    private void initStartDateWithNow() {
        Boolean initVisitRealizationTime = Boolean.valueOf( ClientPropertyReader.getString( "user.ui.quick-visit-view.initialize-realization-date" ));
        if (initVisitRealizationTime){
            viewStartDtProperty.set(LocalDate.now());
            }
    }

    private void loadDict() {
        ListLoader.source(dictService).loadValuesToFxList("PRSPERF", performerValues);
        ListLoader.source(dictService).loadValuesToFxList("PRSREGI", registrantValues);
    }

    void initRegistrant(ComboBox registantCombBox) {
        PositionFx userPosition = getPerformerValues()
                .get()
                .stream()
                .filter(pos -> pos.getCode().equals( Session.instance().getUserLogin() )  )
                .findFirst()
                .orElseThrow(() -> {
                            IllegalStateException illegalStateException = new IllegalStateException("No matching position found for the current user. Check if the value exists in the dictionary, and consider adding it!");
                            logger.error(illegalStateException);
                            throw illegalStateException;
                        }
                );
        registantCombBox.setValue(userPosition);
        selectUserByLogin(userPosition.getCode());

    }

    public void selectUserByLogin(String login) {
        Optional<User> userByLogin = userService.getUserByLogin(login);
        userByLogin.ifPresent(user -> getSelectedPerformer().set( UserVmMapper.of(user)));
    }

    void initPerformer(ComboBox perfomerComboBox) {
        PositionFx userPosition = getPerformerValues()
                .get()
                .stream()
                .filter(pos -> pos.getCode().equals( Session.instance().getUserLogin()))
                .findFirst()
                .orElseThrow();
        perfomerComboBox.setValue(userPosition);
        selectUserByLogin(userPosition.getCode());

    }

    public void setViewStartTimeProperty(LocalTime viewStartTimeProperty) {
        this.viewStartTimeProperty.set(viewStartTimeProperty);
    }




}
