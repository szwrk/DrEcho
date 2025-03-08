package net.wilamowski.drecho.client.application.infra;

import javafx.util.Callback;
import net.wilamowski.drecho.client.presentation.complex.quickvisit.QuickVisitController;
import net.wilamowski.drecho.client.presentation.complex.visits.VisitSearcherView;
import net.wilamowski.drecho.client.presentation.examinations.chooser.ExaminationsChooserController;
import net.wilamowski.drecho.client.presentation.login.LoginController;
import net.wilamowski.drecho.client.presentation.main.MainController;
import net.wilamowski.drecho.client.presentation.main.WelcomeController;
import net.wilamowski.drecho.client.presentation.notes.NotesController;
import net.wilamowski.drecho.client.presentation.patients.PatientRegisterController;
import net.wilamowski.drecho.client.presentation.patients.PatientsSearcherController;
import net.wilamowski.drecho.client.presentation.settings.SettingsController;
import net.wilamowski.drecho.client.presentation.visit.VisitController;
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

  /**
   * Class implements lazy loading using Supplier<Object>, ensuring that controllers are
   * instantiated only when needed.
   */
  public ControllerFactory(
      ViewModelConfiguration viewModelConfiguration, GeneralViewHandler viewHandler) {

    PatientsSearcherController patientsSearcherController =
        new PatientsSearcherController(viewModelConfiguration, viewHandler);
    //
    controllerSuppliers.put(
        LoginController.class,
        () -> new LoginController(viewModelConfiguration.loginViewModel(), viewHandler));
    controllerSuppliers.put(
        MainController.class, () -> new MainController(viewModelConfiguration, viewHandler));
    controllerSuppliers.put(WelcomeController.class, () -> new WelcomeController());
    controllerSuppliers.put(
        SettingsController.class,
        () -> new SettingsController(viewModelConfiguration.settingsViewModel()));
    controllerSuppliers.put(
        QuickVisitController.class,
        () ->
            new QuickVisitController(
                viewHandler,
                new VisitController(viewModelConfiguration.visitViewModel()),
                patientsSearcherController,
                new ExaminationsChooserController(),
                new NotesController()));
    controllerSuppliers.put(PatientsSearcherController.class, () -> patientsSearcherController);
    controllerSuppliers.put(
        PatientRegisterController.class,
        () ->
            new PatientRegisterController(
                viewHandler, viewModelConfiguration.patientRegistrationViewModel()));
    controllerSuppliers.put(
        VisitController.class, () -> new VisitController(viewModelConfiguration.visitViewModel()));
    controllerSuppliers.put(
        VisitSearcherView.class,
        () -> new VisitSearcherView(viewHandler, viewModelConfiguration.visitDashboardViewModel()));
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
