package net.wilamowski.drecho.connectors.model;

import java.util.List;
import net.wilamowski.drecho.connectors.configuration.SettingValue;

public interface ConnectorConfiguration {
  void save(SettingValue s);

  String read(String key);

  List<SettingValue> getAllSettings();

  List<SettingValue> getSettingsByContextName(String contextName);
}
