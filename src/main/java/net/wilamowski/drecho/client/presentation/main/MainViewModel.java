package net.wilamowski.drecho.client.presentation.main;

import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.controler_init.ViewModelOnly;

@ToString
public class MainViewModel implements ViewModelOnly {
  private final ConnectorMainModel dataModel;

  public MainViewModel(ConnectorMainModel dataModel) {
    this.dataModel = dataModel;
  }

  public ConnectorMainModel getDataModel() {
    return dataModel;
  }
}
