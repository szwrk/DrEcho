package net.wilamowski.drecho.connectors.model.standalone.persistance;

import java.util.List;
import net.wilamowski.drecho.connectors.configuration.SettingValue;

public interface ConfigurationRepository {
  void saveProperty(SettingValue s);

  String readProperty(String key);

  List<SettingValue> readAllProperty();

  List<SettingValue> readByContextName(String contextName);
}
