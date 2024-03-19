package net.wilamowski.drecho.connectors.model.standalone.domain.dictionary;

import lombok.ToString;

@ToString
public class Position {
  private final String code;
  private final String name;
  private int order;
  private boolean isActive;

  public Position(String code, String name, int order, boolean isActive) {
    this.code = code;
    this.name = name;
    this.order = order;
    this.isActive = isActive;
  }

  public static Position of(String code , String name , int order) {
    return new Position(code, name, order, true);
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public boolean isActive() {
    return isActive;
  }

  public void enable() {
    isActive = true;
  }

  public void disable() {
    isActive = false;
  }

  public void changePositionNumber(int order) {
    this.order = order;
  }

  public int getOrder() {
    return order;
  }
}
