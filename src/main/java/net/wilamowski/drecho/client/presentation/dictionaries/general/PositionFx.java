package net.wilamowski.drecho.client.presentation.dictionaries.general;

import java.util.Objects;
import javafx.beans.property.*;
import lombok.ToString;
import net.wilamowski.drecho.standalone.domain.dictionary.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString
public class PositionFx {
  private static final Logger logger = LogManager.getLogger(PositionFx.class);
  private final StringProperty code;
  private final StringProperty name;
  private final IntegerProperty order;
  private final BooleanProperty active;

  public PositionFx(Position position) {
    this.code = new SimpleStringProperty(position.getCode());
    this.name = new SimpleStringProperty(position.getName());
    this.order = new SimpleIntegerProperty(position.getOrder());
    this.active = new SimpleBooleanProperty(position.isActive());
  }

  public void enable() {
    active.set(true);
  }

  public void disable() {
    active.set(false);
  }

  public String getCode() {
    return code.get();
  }

  public void setCode(String code) {
    this.code.set(code);
  }

  public StringProperty codeProperty() {
    return code;
  }

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public StringProperty nameProperty() {
    return name;
  }

  public int getOrder() {
    return order.get();
  }

  public void setOrder(int order) {
    this.order.set(order);
  }

  public IntegerProperty orderProperty() {
    return order;
  }

  public boolean getActive() {
    return active.get();
  }

  public void setActive(boolean active) {
    this.active.set(active);
  }

  public BooleanProperty activeProperty() {
    return active;
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PositionFx that = (PositionFx) o;
    return Objects.equals(code, that.code);
  }
}
