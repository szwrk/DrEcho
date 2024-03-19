package poc.GenericDocumentGeneratorPoc;

import java.io.*;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class BudowanieElementowTestApp {

  @Test
  public void zamienWierszeNaHBoxy() {
    List<String> lines = readLinesFromFile();
    String result = buildingTemplate(lines);
    System.out.println(result);
  }

  private String buildingTemplate(List<String> lines) {
    StringBuilder s = new StringBuilder();
    for (String readedValue : lines) {
      if (!readedValue.contains("#") && !readedValue.contains("%")) {
        String prefix = "%"; // for bundle
        String key = prefix + readedValue;
        s.append(hbox(key));
        s.append(children());
        s.append(label(key));
        s.append(hboxMargin(key));
        s.append(insets(key));
        s.append(hboxMarginClose());
        s.append(labelMarginClose());
        s.append(textField(key));
        s.append(childrenClose());
        s.append(hboxClose());
      }
    }
    return s.toString();
  }

  private static String hboxClose() {
    return "            </HBox>\n";
  }

  private static String childrenClose() {
    return "               </children>\n";
  }

  private static String textField(String fxId) {
    return String.format(
        "               <TextField fx:id=\"%1$s\" prefWidth=\"500.0\" promptText=\"%1$s\" />\n",
        fxId);
  }

  private static String labelMarginClose() {
    return "                  </Label>\n";
  }

  private static String hboxMarginClose() {
    return "                     </HBox.margin>\n";
  }

  private static String insets(String key) {
    return "                        <Insets bottom=\"5.0\" left=\"5.0\" right=\"5.0\" top=\"5.0\" />\n";
  }

  private static String hboxMargin(String key) {
    return "                     <HBox.margin>\n";
  }

  private static String label(String key) {
    return String.format(
        "                  <Label alignment=\"CENTER_RIGHT\" minWidth=\"100.0\" prefWidth=\"200.0\" text=\"%1$s\">\n",
        key);
  }

  private static String children() {
    return "               <children>\n";
  }

  private static String hbox(String key) {
    return "      <HBox prefHeight=\"28.0\" prefWidth=\"245.0\">\n";
  }

  private static List<String> readLinesFromFile() {
    final String path =
        "C://Users//Arek//_PROJEKTY//_echokardioLg//echo_tte_etykiety_angielskie.properties";
    File file = new File(path);
    List<String> lines = null;
    try {
      lines = FileUtils.readLines(file, "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return lines;
  }
  // String TEMPLATE = "Text: [%{text}] Number: [%{number}] Text again: [%{text}]";
  // Map<String, Object> params = new HashMap<>();
  // params.put("text", "'${number}' is a placeholder.");
  // params.put("number", 42);
  // String result = StringSubstitutor.replace(TEMPLATE, params, "%{", "}");
  //
  // assertThat(result).isEqualTo("Text: ['${number}' is a placeholder.] Number: [42] Text again:
  // ['${number}' is a placeholder.]");

}
