package net.wilamowski.drecho.client.presentation.customs.inputfilter;

import javafx.util.StringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class Converters {

  public StringConverter createConverterBy(String s) {
    switch (s) {
      case "INTEGER_3":
        return new IntegerStringConverter();
      case "FLOAT":
        return new FloatStringConverter();
      case "DOUBLE":
        return doubleNotNullConverterInstance();
      case "TEXT_UPPER":
        return upperStringConverterInstance();
      case "TEXT":
        return simpleStringConverterInstance();
      case "TIME":
        return simpleStringConverterInstance();
      default:
        throw new IllegalStateException("Unexpected value: " + s);
    }
  }

  StringConverter<String> upperStringConverterInstance() {
    return new StringConverter<String>() {
      @Override
      public String toString(String obj) {
        if (obj == null) {
          return "";
        }
        return obj.toUpperCase();
      }

      @Override
      public String fromString(String obj) {
        if (obj == null) {
          return "";
        }
        return obj.toLowerCase();
      }
    };
  }

  StringConverter<String> simpleStringConverterInstance() {
    return new StringConverter<String>() {
      @Override
      public String toString(String s) {
        return s;
      }

      @Override
      public String fromString(String s) {
        return s;
      }
    };
  }

  StringConverter<Double> doubleNotNullConverterInstance() {
    return new StringConverter<Double>() {
      @Override
      public String toString(Double value) {
        return (value == null) ? "" : value.toString();
      }

      @Override
      public Double fromString(String text) {
        return (text == null || text.trim().isEmpty()) ? null : Double.parseDouble(text.trim());
      }
    };
  }

  //  StringConverter<String> integerStringConverterNullable() {
  //    return new StringConverter<>() {
  //      @Override
  //      public String toString(String value) {
  //        return (value == null) ? "" : value.toString();
  //      }
  //
  //      @Override
  //      public String fromString(String text) {
  //        return (text == null || text.trim().isEmpty()) ? null : text.trim();
  //      }
  //    };
  //  }
  StringConverter<Integer> integerStringConverterNullable() {
    return new StringConverter<>() {
      @Override
      public String toString(Integer value) {
        return (value == null) ? "" : value.toString();
      }

      @Override
      public Integer fromString(String text) {
        return isEmpty(text) ? null : Integer.parseInt(text.trim());
      }

      private static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.equals("");
      }
    };
  }
}
