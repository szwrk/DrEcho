package net.wilamowski.drecho.client.presentation.customs;

import atlantafx.base.theme.Styles;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.wilamowski.drecho.client.application.infra.ControllerInitializer;
import net.wilamowski.drecho.client.application.infra.GeneralViewHandler;
import net.wilamowski.drecho.client.presentation.main.ViewHandlerInitializer;
import net.wilamowski.drecho.shared.bundle.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class SimpleModalController implements Initializable, ViewHandlerInitializer {
  private static final Logger logger = LogManager.getLogger(SimpleModalController.class);
  @FXML private Pane root;
  @FXML private HBox buttonBar;
  private Parent includedNode;
  private Object includedController;
  private URL url;
  private GeneralViewHandler viewHandler;
  private Stage stage;
  private ResourceBundle bundle;

  public SimpleModalController() {
  }

  public void loadNestedExamByFxmlPath(String subView) {
    logger.debug("Loading nested examination. Param: {}", subView);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + subView));
    loader.setResources(Lang.getBundle( ));
    try {
      includedNode = loader.load();
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    includedController = loader.getController();
    ControllerInitializer initializer = GeneralViewHandler.initializer();
    initializer.initControllers(includedController, viewHandler);
    this.root.getChildren().add(includedNode);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.url = url;
    this.bundle = Lang.getBundle( );
  }

  @Override
  public void initializeViewHandler(GeneralViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  public Object getIncludedController() {
    return includedController;
  }

  public void addConfirmButton(EventHandler e) {
    Button okBtn = new Button("OK");
    okBtn.getStyleClass().addAll(Styles.ACCENT);
    okBtn.setOnAction( e );
    buttonBar.getChildren().add(okBtn);
  }

  public void closeWindow() {
    stage = (Stage) root.getScene().getWindow();
    stage.close();
  }
}
