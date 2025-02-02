package net.wilamowski.drecho.standalone.domain.patient.validations;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {
  private static final Logger logger = LogManager.getLogger(Validator.class);
  private final List<Constraint> constraintList;

  public Validator(List<Constraint> constraintList) {
    this.constraintList = constraintList;
  }

  public static Validator instance() {
    return new Validator(new ArrayList<>());
  }

  public void registerValidations(Constraint constraint) {
    constraintList.add(constraint);
  }

  public List<String> validateAll() {
    List<String> errors = null;
    if (constraintList.isEmpty()) {
      // consume
      logger.warn("Detected use of Validator without any added validations.");
    } else {
      errors = new ArrayList<>();
      for (Constraint v : constraintList) {
        if (!v.validate()) {
          errors.add(v.errorMessage());
        }
      }
    }
    return errors;
  }
}
