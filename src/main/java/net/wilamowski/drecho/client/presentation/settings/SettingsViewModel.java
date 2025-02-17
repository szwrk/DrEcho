package net.wilamowski.drecho.client.presentation.settings;

import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.SettingVmMapper;
import net.wilamowski.drecho.app.configuration.SettingValue;
import net.wilamowski.drecho.configuration.backend_ports.SettingsService;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
public class SettingsViewModel {
  private final SettingsService model;
  private ObservableList<SettingPropertyFx> settingsFx = FXCollections.observableArrayList();

  public SettingsViewModel(SettingsService model) {
    this.model = model;
    init();
  }

  public void init() {
    List<SettingValue> settings = model.getSettingsByContextName("admin.");
    SettingVmMapper mapper = new SettingVmMapper();
    List<SettingPropertyFx> settingsVmBeans =
        settings.stream().map(s -> mapper.toFxBean(s)).collect(Collectors.toList());
    settingsFx = FXCollections.observableList(settingsVmBeans);
  }

  public ObservableList<SettingPropertyFx> getSettingsFx() {
    return settingsFx;
  }

  public void update(SettingPropertyFx singleSetting) {
    SettingVmMapper settingMapper = new SettingVmMapper();
    SettingValue setting = settingMapper.toDomain(singleSetting);
    model.save(setting);
  }
}
