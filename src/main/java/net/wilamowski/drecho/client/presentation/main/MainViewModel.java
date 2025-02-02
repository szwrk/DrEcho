package net.wilamowski.drecho.client.presentation.main;

import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.controler_init.ViewModelOnly;

@ToString
public class MainViewModel implements ViewModelOnly {
  private final MainService dataModel;

  public MainViewModel(MainService dataModel) {
    this.dataModel = dataModel;
  }

  public MainService getDataModel() {
    return dataModel;
  }
}
