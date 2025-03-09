package arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.Test;

public class FxmlLoaderUsageTest {
  JavaClasses classes = new ClassFileImporter().importPackages("net.wilamowski.drecho");

  @Test
  public void fxmlLoaderShouldOnlyBeUsedInGeneralViewHandler() {
    ArchRule rule = noClasses()
            .that()
            .resideOutsideOfPackage("net.wilamowski.drecho.client.application.infra")
            .should()
            .dependOnClassesThat()
            .areAssignableTo( FXMLLoader.class);
    rule.check(classes);
  }
}
