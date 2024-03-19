package net.wilamowski.drecho.client.presentation.customs.inputfilter;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;
import net.wilamowski.drecho.client.properties.ClientPropertyReader;

public class Formatters {
  public static final String FORMAT_REGEX_INTEGER = "^[0-9]\\d{0,%d}$";
  private final Double defaultDouble = null;
  private Integer defaultInt = null;
  private Float defaultFloat = null;
  private String defaultString = null;
  private Converters converters = null;

  private Formatters() {
    this.converters = new Converters();
    turnOnDefaultValue();
  }

  private void turnOnDefaultValue() {
    if (Boolean.parseBoolean(ClientPropertyReader.getString("admin.debug.form.default"))) {
      //      this.defaultInt =
      // Integer.valueOf(PropertyReader.get("admin.admin.debug.form.default.int"));
      this.defaultInt =
          Integer.valueOf(ClientPropertyReader.getString("admin.debug.form.default.int"));
      this.defaultFloat =
          Float.valueOf(ClientPropertyReader.getString("admin.debug.form.default.float"));
      this.defaultString = ClientPropertyReader.getString("admin.debug.form.default.string");
    }
  }

  public static Formatters newFactory() {
    return new Formatters();
  }

  public TextFormatter createFormatterBy(String s) {
    switch (s) {
      case "INTEGER":
        return newIntegerFormatter(defaultInt, 15);
      case "INTEGER_3":
        return newIntegerFormatter(defaultInt, 2);
      case "INTEGER_4":
        return newIntegerFormatter(defaultInt, 3);
      case "FLOAT":
        return new TextFormatter<Float>(new FloatStringConverter(), defaultFloat);
      case "DOUBLE":
        return new TextFormatter<Double>(
            converters.doubleNotNullConverterInstance(), defaultDouble);
      case "TEXT_UPPER":
        return new TextFormatter<String>(converters.upperStringConverterInstance(), defaultString);
      case "TEXT":
        return new TextFormatter<String>(converters.simpleStringConverterInstance(), defaultString);
      case "TIME":
        return new TextFormatter<String>(converters.simpleStringConverterInstance(), null);
      default:
        throw new IllegalStateException("Unexpected value: " + s);
    }
  }

  private TextFormatter newIntegerFormatter(Integer defaultValue, int integerLength) {
    String regex = String.format(FORMAT_REGEX_INTEGER, integerLength);
    UnaryOperator<TextFormatter.Change> integerFilter =
        createIntegerFilterForBlockingUserInput(regex);
    return new TextFormatter<>(
        converters.integerStringConverterNullable(), defaultValue, integerFilter);
    //    return new TextFormatter<>(converters.integerStringConverterNullable(), defaultValue,
    // integerFilter);
  }

  private static UnaryOperator<TextFormatter.Change> createIntegerFilterForBlockingUserInput(
      String regexpFilter) {
    return change -> {
      String newText = change.getControlNewText();
      if (isEmptyChange(newText)) {
        if (newText.matches(regexpFilter)) {
          return change;
        } else {
          return change;
        }
      } else if (!isEmptyChange(newText)) {
        if (isNegativeInteger(newText)) {
          change.setText("");
          return change;
        } else return change;
      }
      //      if (isBackspacePressed(newText) || newText.matches(regexpFilter)) {
      //        return change;
      //      } else if (isNegativeInteger(newText)) {
      //        change.setText("");
      //        return change;
      //      }
      return null;
    };
  }

  private static boolean isNegativeInteger(String newText) {
    return newText.startsWith("-");
  }

  private static boolean isEmptyChange(String newText) {
    return newText.isEmpty();
  }
}
