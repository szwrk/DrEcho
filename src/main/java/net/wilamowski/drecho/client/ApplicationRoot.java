package net.wilamowski.drecho.client;

import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;
import net.wilamowski.drecho.client.application.exceptions.old.MyDefaultUncaughtExceptionHandler;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.application.infra.ViewModelConfiguration;
import net.wilamowski.drecho.client.presentation.customs.modals.ExceptionAlert;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;
import net.wilamowski.drecho.connectors.model.standalone.infra.ConnectorLayer;
import net.wilamowski.drecho.connectors.model.standalone.infra.DeploymentType;
import net.wilamowski.drecho.connectors.model.standalone.infra.ModelLayerFactory;
import net.wilamowski.drecho.shared.auth.Session;
import net.wilamowski.drecho.shared.bundle.Lang;
import net.wilamowski.drecho.shared.bundle.ResourceBundleFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class ApplicationRoot extends Application {
  public static final String FILE_ENCODING = "file.encoding";
  public static final String UTF_8 = "UTF-8";
  private static final Logger log = LogManager.getLogger(ApplicationRoot.class);
  private static final Session currentSession = Session.instance();
  private GeneralViewHandler generalViewHandler;
  private DeploymentType deployMode;

  @Override
  public void init() {
    log.info("MedNoteApp initialize...");
    setSystemFileEncoding();
    configureUncaughtExceptionHandler();
    loadDeployMode();
  }

  private void loadDeployMode() {
    log.info("Loading deploy mode...");
    String string = ClientPropertyReader.getString("admin.backend-connect-mode");
    deployMode = DeploymentType.of(string);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    log.info("Application starting...");
    initializeLanguage();
    ConnectorLayer model = Objects.requireNonNull(createModelLayer());
    initializeViewHandler(model);
    Objects.requireNonNull(generalViewHandler);

    generalViewHandler.openLogin();
  }

  private void initializeLanguage() {
    ResourceBundle mainAppBundle =
        ResourceBundleFactory.instanceMainApp(ClientPropertyReader.getString("user.ui.language"));
    Lang.initializeSingleton(Objects.requireNonNull(mainAppBundle));
  }

  private ConnectorLayer createModelLayer() {
    ConnectorLayer backend = null;
    ModelLayerFactory modelLayerFactory = new ModelLayerFactory();
    try {
      backend = modelLayerFactory.createModelLayerByType(deployMode);
    } catch (IllegalArgumentException e) {
      handleException(e, "e.002.header", "e.002.msg", deployMode.toString());
    } catch (Exception e) {
      handleException(e, "e.999.header", "e.999.msg");
    }
    return backend;
  }

  //  public void startContext(String deployModel) {
  //    log.info("Creating new instance of MedNoteApp. Deploy mode: {}", deployModel);
  //    try{
  //    this.deployMode = "";
  //    try {
  //      this.deployMode = Objects.requireNonNull(deployModel);
  //    } catch (NullPointerException npe) {
  //      handleException(npe, "e.004.header", "e.004.msg", "'tryb backendu'");
  //    }
  //
  //  } catch (Exception e) {
  //    log.error(e.getMessage(), e);
  //    throw new RuntimeException(e);
  //  }
  //  }

  private void handleException(
      Exception e, String headerKey, String msgKey, String stringFormatParameter) {
    log.error(e.getMessage(), e);
    String header = Lang.getString(headerKey);
    String msg = Lang.getString(msgKey);
    ExceptionAlert.create().showError(e, header, String.format(msg, stringFormatParameter));
  }

  private void handleException(Exception e, String headerKey, String msgKey) {
    log.error(e.getMessage(), e);
    String header = Lang.getString(headerKey);
    String msg = Lang.getString(msgKey);
    ExceptionAlert.create().showError(e, header, msg);
  }

  private void initializeViewHandler(ConnectorLayer backend) {
    log.debug("Initializing GeneralViewHandler...");
    String                 globalStyleName        = ClientPropertyReader.getString("user.style.set");
    ViewModelConfiguration viewModelConfiguration = new ViewModelConfiguration(backend);
    generalViewHandler = GeneralViewHandler.instance(globalStyleName, viewModelConfiguration , this);
    log.debug("Initializing GeneralViewHandler... DONE");
  }

  @Override
  public void stop() throws Exception {
    log.info("Application stop...");
  }

  private void setSystemFileEncoding() {
    log.info(FILE_ENCODING, UTF_8);
    System.setProperty(FILE_ENCODING, UTF_8);
  }

  private void configureUncaughtExceptionHandler() {
    MyDefaultUncaughtExceptionHandler myDefaultUncaughtExceptionHandler =
        new MyDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(myDefaultUncaughtExceptionHandler);
    log.debug("Set default uncaught exception handler");
  }
}
