package arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "net.wilamowski.drecho")
public class ClientSideTest {
    JavaClasses classes = new ClassFileImporter().importPackages("net.wilamowski.drecho");
  /**MVVM. Controller cannot invoke service layer directly.
   * OK: View -> Service
   * NOT OK: View -> ViewModel -> Service
   * */

  @Test
  public void controllersCannotUseBackendDirectly() {
      ArchRule rule = noClasses( )
              .that( )
              .haveSimpleNameContaining( "Controller" )
              .should( )
              .accessClassesThat( )
              .resideInAPackage( "..connectors.." );
      rule.check( classes );
  }
}
