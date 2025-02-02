package net.wilamowski.drecho.client.presentation.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectorMainModelStandalone implements MainService {
  private static final Logger log = LogManager.getLogger( ConnectorMainModelStandalone.class);
  private String fieldValue;

  //    private MainModelRepository repository;
  //
  //    public MainModelStandalone(MainModelRepository repository) {
  //        this.repository = repository;
  //    }

  @Override
  public void saveField(String fieldValue) {
    this.fieldValue = fieldValue;
    log.debug(this.toString());
  }

  @Override
  public String toString() {
    return "MainDataModelInMemory{" + "fieldValue='" + fieldValue + '\'' + '}';
  }
}
