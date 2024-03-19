package net.wilamowski.drecho.connectors.model.standalone.domain.patient;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class Patient {
  private Long id;
  private final String name;
  private final String lastName;
  @ToString.Exclude private final String pesel;
  private String nameOfCityBirth;
  private String codeOfCityBirth;
  private LocalDate dateBirth;
  private String generalPatientNote;
  private String patientTelephoneNumber;
  @ToString.Include(name="pesel")
  private String hidePesel(){
    String mask  = "***********";
    if (pesel == null || pesel.isEmpty()) {
      return "Unknown";
    }
    return mask.substring( 0,pesel.length() );
  }
}
