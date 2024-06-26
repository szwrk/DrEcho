package net.wilamowski.drecho.connectors.model.standalone.domain.patient.validations;

import java.util.List;
import net.wilamowski.drecho.connectors.model.ConnectorPatient;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.shared.dto.PatientDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class PatientPeselUniqueConstraint implements Constraint {
  private static final Logger logger = LogManager.getLogger(PatientPeselUniqueConstraint.class);

  private final ConnectorPatient service;
  private final Object object;

  public PatientPeselUniqueConstraint(ConnectorPatient service, Object validatedObject) {
    this.service = service;
    this.object = validatedObject;
  }

  public boolean validate() {
    Patient          patient     = (Patient) object;
    List<PatientDto> findByPesel = service.findByPesel(patient.getPesel(), 0);
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
