package net.wilamowski.drecho.client.application.infra;

import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import net.wilamowski.drecho.client.application.infra.controler_init.KeyEventDebugInitializer;
import net.wilamowski.drecho.client.application.infra.controler_init.PostInitializable;
import net.wilamowski.drecho.client.application.infra.controler_init.Tooltipable;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.client.presentation.tooltiper.AutoTooltiper;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.app.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class ControllerInitializerImpl implements ControllerInitializer {
  public static final String DEBUG_CONTROLS_HOVER_TOOLTIPS = "user.debug.controls-hover-tooltips";
  private static final Logger logger = LogManager.getLogger(ControllerInitializerImpl.class);
  private final ViewModelConfiguration viewModelConfiguration;

  public ControllerInitializerImpl(ViewModelConfiguration viewModelConfiguration) {
    Objects.requireNonNull(Lang.getBundle(), "Bundle is null");
    Objects.requireNonNull( viewModelConfiguration , "ViewModels is null");
    this.viewModelConfiguration = viewModelConfiguration;
  }

  @Override
  public void initController(Object controller, GeneralViewHandler handler) {
    defaultInit(controller, handler);
  }

  private void defaultInit(Object controller, GeneralViewHandler handler) {
    Objects.requireNonNull(
        controller,
        "Controller is null. Possible reasons: view location (or name) is not correct; fxml without defined controller");
    Objects.requireNonNull(handler, "Handler is null.");

    if (controller instanceof ViewHandlerInitializer handlerController) {
      handlerController.initializeViewHandler(handler);
    } else {
      logger.debug(
          "Controller {} is not instance of {} ",
          controller.getClass(),
          ViewHandlerInitializer.class);
    }

    if (controller instanceof Initializable initializeController) {
      initializeController.initialize(null, null);
    } else {
      logger.debug(
          "Controller {} is not instance of {} ", controller.getClass(), Initializable.class);
    }

    if (controller instanceof ViewModelsInitializer viewModelsInitializer) {
      viewModelsInitializer.initializeViewModels(
          Objects.requireNonNull( viewModelConfiguration , "View Model factory is null!"));
    } else {
      logger.debug(
          "Controller {} is not instance of {} ",
          controller.getClass(),
          ViewModelsInitializer.class);
    }

    if (controller instanceof KeyEventDebugInitializer debugableController) {
      debugableController.initializeKeyEventDebugging();
    } else {
      logger.debug(
          "Controller {} is not instance of {} ",
          controller.getClass(),
          KeyEventDebugInitializer.class);
    }

    if (controller instanceof Tooltipable tooltipable) {
      Node root = tooltipable.getRootUiNode();
      logger.traceEntry("Initialize tooltipers...");
      runAutoTooltiper(Lang.getBundle(), root);
      logger.traceExit();
    } else {
      logger.debug(
          "Controller {} is not instance of {} ", controller.getClass(), Tooltipable.class);
    }
    if (controller instanceof PostInitializable postInitController) {
      postInitController.postInitialize();
    } else {
      logger.debug(
          "Controller {} is not instance of {} ", controller.getClass(), PostInitializable.class);
    }
  }

  private static void runAutoTooltiper(ResourceBundle bundle, Node root) {
    if (ClientPropertyReader.getString(DEBUG_CONTROLS_HOVER_TOOLTIPS).equals("true"))
      try {
        AutoTooltiper autoTooltiper = new AutoTooltiper();
        autoTooltiper.runTree(root);
      } catch (Exception e) {
        logger.error("Error creating tooltips in postInitialize: {}", e.getMessage(), e);
      }
  }
}
