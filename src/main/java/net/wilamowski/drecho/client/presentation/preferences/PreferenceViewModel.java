package net.wilamowski.drecho.client.presentation.preferences;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.wilamowski.drecho.client.application.mapper.SettingVmMapper;
import net.wilamowski.drecho.client.presentation.settings.SettingPropertyFx;
import net.wilamowski.drecho.connectors.configuration.SettingValue;
import net.wilamowski.drecho.connectors.model.Configuration;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class PreferenceViewModel {
    private final Configuration model;
    private ObservableList<SettingPropertyFx> settingsFx = FXCollections.observableArrayList();

    public PreferenceViewModel(Configuration model) {
        this.model = model;
        init();
    }

    public void init() {
        List<SettingValue> settings = model.getAllSettings();
        SettingVmMapper    mapper   = new SettingVmMapper();
        List<SettingPropertyFx> settingsVmBeans =
                settings.stream().map(s -> mapper.toFxBean(s)).collect( Collectors.toList());
        settingsFx = FXCollections.observableList(settingsVmBeans);
    }

    public ObservableList<SettingPropertyFx> getSettingsFx() {
        return settingsFx;
    }

    public void update(SettingPropertyFx singleSetting) {
        SettingVmMapper settingMapper = new SettingVmMapper();
        SettingValue    setting       = settingMapper.toDomain(singleSetting);
        model.save(setting);
    }
}


