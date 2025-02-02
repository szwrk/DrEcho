package net.wilamowski.drecho.configuration.backend_ports;

import java.util.List;
import net.wilamowski.drecho.app.configuration.SettingValue;

public interface SettingsService {
  void save(SettingValue s);

  String read(String key);

  List<SettingValue> getAllSettings();

  List<SettingValue> getSettingsByContextName(String contextName);
}
