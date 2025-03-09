package net.wilamowski.drecho.standalone.domain.patient.validations;

import java.util.List;
import net.wilamowski.drecho.app.dto.PatientDto;
import net.wilamowski.drecho.configuration.backend_ports.PatientService;
import net.wilamowski.drecho.standalone.domain.patient.Patient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class PatientPeselUniqueConstraint implements Constraint {
  private static final Logger logger = LogManager.getLogger(PatientPeselUniqueConstraint.class);

  private final PatientService service;
  private final Object object;

  public PatientPeselUniqueConstraint(PatientService service, Object validatedObject) {
    this.service = service;
    this.object = validatedObject;
  }

  public boolean validate() {
    Patient patient = (Patient) object;
    if (patient.getPesel() == null) {
      return false;
    }
    List<PatientDto> findByPesel = service.findByCitizenCode(patient.getPesel(), 0);

    if (findByPesel.isEmpty()) { // not using code so ok
      logger.debug("[SERVICE-PATIENT] Pesel code validation... OK");
      return true;
    } else {
      logger.debug("[SERVICE-PATIENT] Pesel code validation... FAILED");
      return false;
    }
  }

  @Override
  public String errorMessage() {
    return "This PESEL code is already registered with another patient. Please provide a different PESEL code. ";
  }
}
