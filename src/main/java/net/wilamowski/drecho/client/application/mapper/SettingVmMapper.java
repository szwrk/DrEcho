package net.wilamowski.drecho.client.application.mapper;

import net.wilamowski.drecho.client.presentation.settings.SettingPropertyFx;
import net.wilamowski.drecho.connectors.configuration.SettingValue;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class SettingVmMapper {
  public SettingPropertyFx toFxBean(SettingValue setting) {
    return new SettingPropertyFx(setting.getKey(), setting.getValue());
  }

  public SettingValue toDomain(SettingPropertyFx singleSetting) {
    return new SettingValue(singleSetting.getKey().get(), singleSetting.getValue().get());
  }
}
