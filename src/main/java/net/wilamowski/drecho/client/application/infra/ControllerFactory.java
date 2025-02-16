package net.wilamowski.drecho.client.application.infra;

import javafx.util.Callback;
import net.wilamowski.drecho.client.presentation.login.LoginController;
import net.wilamowski.drecho.client.presentation.main.MainController;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/
public class ControllerFactory implements Callback<Class<?>, Object> {
    private final Map<Class<?>, Supplier<Object>> controllerSuppliers = new HashMap<>();

    public ControllerFactory(ViewModelConfiguration viewModelConfiguration, GeneralViewHandler viewHandler) {
        controllerSuppliers.put( LoginController.class,
                () -> new LoginController( viewModelConfiguration.loginViewModel(), viewHandler));
        controllerSuppliers.put( MainController.class, MainController::new );
    }

    @Override
    public Object call(Class<?> aClass) {
        Supplier<Object> supplier = controllerSuppliers.get( aClass );
      if (supplier!=null){
          return supplier.get();
          }
      throw new IllegalArgumentException( "Controller class not found!" );
      }
    }

