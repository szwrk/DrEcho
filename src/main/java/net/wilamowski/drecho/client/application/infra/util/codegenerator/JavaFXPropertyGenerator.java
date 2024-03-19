package net.wilamowski.drecho.client.application.infra.util.codegenerator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net JavaFXPropertyGenerator is a
 *     utility class responsible for generating JavaFX properties based on text input. It offers
 *     methods to create different types of JavaFX properties and binding expressions, simplifying
 *     the development of JavaFX applications.
 */
public class JavaFXPropertyGenerator {
  public String extractNameFromBundle(List<String> list) {
    return list.stream()
        .filter(JavaFXPropertyGenerator::isCommented)
        .map(e -> e.split("=")[0])
        .collect(Collectors.joining("\n"));
  }

  private static boolean isCommented(String e) {
    return !e.startsWith("#");
  }

  public String extractNameFromBundleAndAddStringBinding(List<String> list) {
    return list.stream()
        .filter(JavaFXPropertyGenerator::isCommented)
        .map(e -> e.split("=")[0])
        .map(
            e ->
                String.format(
                    "%sTextField.textProperty().bindBidirectional(viewModel.%sProperty();", e, e))
        .collect(Collectors.joining("\n"));
  }

  public String extractNameFromBundleAndAutoAddBinding(List<String> list) {
    return list.stream()
        .filter(e -> blankRow(e) || isNotLabel(e))
        .filter(JavaFXPropertyGenerator::isCommented)
        .map(e -> e.split("=")[0])
        .map(
            e -> {
              if (isTextProperty(e)) {
                return createStringBinding(e);
              } else {
                return createFloatBinding(e);
              }
            })
        .collect(Collectors.joining("\n"));
  }

  private static boolean blankRow(String e) {
    return !e.isBlank();
  }

  private static boolean isTextProperty(String e) {
    return e.contains("txt");
  }

  private static boolean isNotLabel(String e) {
    return !e.contains("lbl");
  }

  private static String createStringBinding(String e) {
    return String.format(
        "%1$s.textProperty().bindBidirectional(viewModel.%1$sProperty());", e, "Text");
  }

  private static String createFloatBinding(String e) {
    return String.format(
        "%1$s.textProperty().bindBidirectional(viewModel.%1$sProperty(), floatStringConverter);",
        e, "Float");
  }

  public String extractNameFromBundleAndCreateProperties(List<String> list) {
    return list.stream()
        .filter(e -> blankRow(e) || isNotLabel(e))
        .filter(JavaFXPropertyGenerator::isCommented)
        .map(e -> e.split("=")[0])
        .map(
            e -> {
              if (isTextProperty(e)) {
                return createStringProperty(e);
              } else {
                return createFloatProperty(e);
              }
            })
        .collect(Collectors.joining("\n"));
  }

  private static String createStringProperty(String e) {
    return String.format("private %2$sProperty %1$s = new SimpleStringProperty( );", e, "String");
  }

  private static String createFloatProperty(String e) {
    return String.format("private %2$sProperty %1$s = new Simple%2$sProperty(  );", e, "Float");
  }
}
