package net.wilamowski.drecho.connectors.model.standalone.persistance.mapper;

import java.util.Optional;
import net.wilamowski.drecho.connectors.model.standalone.domain.patient.Patient;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.PatientNotFoundException;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.UserNotFoundException;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.Visit;
import net.wilamowski.drecho.connectors.model.standalone.domain.visit.VisitEntity;
import net.wilamowski.drecho.connectors.model.standalone.persistance.PatientRepository;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class VisitEntityMapper {
  private final UserRepository userRepository;
  private final PatientRepository patientRepostitory;

  public VisitEntityMapper(UserRepository userRepository, PatientRepository patientRepostitory) {
    this.userRepository = userRepository;
    this.patientRepostitory = patientRepostitory;
  }

  public Visit toDomain(VisitEntity entity) {
    String selectedPerformer = entity.getSelectedPerformer();
    Optional<User> optionalUserPerformer = userRepository.findByLogin(selectedPerformer);

    String selectedRegistrant = entity.getSelectedRegistrant();
    Optional<User> optionalUserRegistrant = userRepository.findByLogin(selectedRegistrant);

    Long patientId = entity.getPatientId();
    Optional<Patient> optionalPatient = patientRepostitory.findById(patientId);

    Visit visit =
        Visit.builder()
            .selectedPerformer(
                optionalUserPerformer.orElseThrow(
                    () ->
                        new UserNotFoundException(
                            "Visit mapper has an issue. Performer user not found")))
            .selectedRegistrant(
                optionalUserRegistrant.orElseThrow(
                    () ->
                        new UserNotFoundException(
                            "Visit mapper has an issue. Registrant user not found")))
            .realizationDateTime(entity.getRealizationDateTimeProperty())
            .viewStartDateTime(entity.getViewStartDateTimeProperty())
            .patient(
                optionalPatient.orElseThrow(
                    () ->
                        new PatientNotFoundException(
                            "Visit mapper has an issue. Registrant user not found")))
            .build();
    return visit;
  }
}
