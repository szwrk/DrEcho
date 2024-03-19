package net.wilamowski.drecho.connectors.model.standalone.persistance;

import net.wilamowski.drecho.connectors.configuration.SettingValue;

import java.util.List;

public interface ConfigurationRepository {
  void saveProperty(SettingValue s);

  String readProperty(String key);

  List<SettingValue> readAllProperty();

    List<SettingValue> readByContextName(String contextName);
}
