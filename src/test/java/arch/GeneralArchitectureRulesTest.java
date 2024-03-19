package arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "net.wilamowski.drecho")
public class GeneralArchitectureRulesTest {
  JavaClasses classes = new ClassFileImporter().importPackages("net.wilamowski.drecho");

  /** General rule. Model can not use view. */
  @Test
  public void backendShouldNotAccessClient() {
    ArchRule rule =
        noClasses()
            .that()
            .resideInAPackage("..connectors..")
            .should()
            .accessClassesThat()
            .resideInAPackage("..client..");

    rule.check(classes);
  }
}
