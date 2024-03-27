package net.wilamowski.drecho.client.presentation.complex.visits;

import java.time.LocalDate;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.PatientDtoVmMapper;
import net.wilamowski.drecho.client.application.mapper.VisitDtoVmMapper;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.visit.VisitVM;
import net.wilamowski.drecho.connectors.model.VisitModel;
import net.wilamowski.drecho.shared.dto.PatientDto;
import net.wilamowski.drecho.shared.dto.VisitDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@Getter
@ToString
public class VisitDashboardViewModel {
  private static final Logger logger = LogManager.getLogger(VisitDashboardViewModel.class);

  private final VisitModel visitModel;
  private final ObservableList<VisitVM> visits = FXCollections.observableArrayList();

  public VisitDashboardViewModel(VisitModel visitModel) {
    this.visitModel = visitModel;
  }

  public void searchByPatient(PatientVM patientVM , int page) {
    logger.trace("Clicked search by selected patient");
    if ( patientVM != null) {
      PatientDto    patient  = PatientDtoVmMapper.toDto( patientVM );
      Set<VisitDto> visitSet = visitModel.listVisitsBy(patient, page);
      loadVisitsToTable(visitSet);
    }
  }

  private void loadVisitsToTable(Set<VisitDto> visitSet) {
    if (visitSet != null) {
      logger.debug("SERVICE - visit service returns: {} entity items", visitSet.size());
      Set<VisitVM> patientsFxBean = VisitDtoVmMapper.toListToVM(visitSet);
      if (patientsFxBean != null) {
        logger.debug("VIEW MODEL - visit mapper returns: {} fx items", patientsFxBean.size());
        updatePatientsTable(patientsFxBean);
      } else {
        logger.debug("patientsFxBean is null");
      }
    } else {
      logger.debug("visitSet is null");
    }
  }

  void updatePatientsTable(Set<VisitVM> visitVMList) {
    visits.clear();
    visits.addAll(visitVMList);
  }

  public void searchByDate(LocalDate date, int page) {
    logger.trace("Clicked search by date");
    Set<VisitDto> visitSet = visitModel.listVisitsBy(date, page);
    loadVisitsToTable(visitSet);
  }

  public void clearSearchResults() {
    visits.clear();
  }
}
