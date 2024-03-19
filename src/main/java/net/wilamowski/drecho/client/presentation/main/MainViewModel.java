package net.wilamowski.drecho.client.presentation.main;

import lombok.ToString;
import net.wilamowski.drecho.client.application.infra.controler_init.ViewModelOnly;

@ToString
public class MainViewModel implements ViewModelOnly {
  private final MainModel dataModel;
  public MainViewModel(MainModel dataModel) {
    this.dataModel = dataModel;
  }

  public MainModel getDataModel() {
    return dataModel;
  }
}
