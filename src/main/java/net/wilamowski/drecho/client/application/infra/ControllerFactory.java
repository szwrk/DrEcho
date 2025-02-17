package net.wilamowski.drecho.client.application.infra;

import javafx.util.Callback;
import net.wilamowski.drecho.client.presentation.login.LoginController;
import net.wilamowski.drecho.client.presentation.main.MainController;
import net.wilamowski.drecho.client.presentation.main.WelcomeController;
import net.wilamowski.drecho.client.presentation.settings.SettingsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/
public class ControllerFactory implements Callback<Class<?>, Object> {
  private static final Logger logger = LogManager.getLogger(MainController.class);
  private final Map<Class<?>, Supplier<Object>> controllerSuppliers = new HashMap<>();

  public ControllerFactory(
      ViewModelConfiguration viewModelConfiguration, GeneralViewHandler viewHandler) {
    controllerSuppliers.put(
        LoginController.class,
        () -> new LoginController(viewModelConfiguration.loginViewModel(), viewHandler));
    controllerSuppliers.put(
        MainController.class, () -> new MainController(viewModelConfiguration, viewHandler));
    controllerSuppliers.put( WelcomeController.class, ()-> new WelcomeController() );

    controllerSuppliers.put( SettingsController.class, ()->new SettingsController( viewModelConfiguration.settingsViewModel() ) );
  }

  @Override
  public Object call(Class<?> aClass) {
    Supplier<Object> supplier = controllerSuppliers.get(aClass);
    if (supplier != null) {
      return supplier.get();
    }
    logger.error("Controller class not found: {}", aClass);
    throw new IllegalArgumentException("Controller class not found!");
  }
}
