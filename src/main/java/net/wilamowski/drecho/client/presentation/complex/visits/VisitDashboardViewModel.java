package net.wilamowski.drecho.client.presentation.complex.visits;

import java.time.LocalDate;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.client.application.mapper.PatientVmMapper;
import net.wilamowski.drecho.client.application.mapper.VisitVmMapper;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;
import net.wilamowski.drecho.client.presentation.visit.VisitVM;
import net.wilamowski.drecho.connectors.infrastructure.VisitDto;
import net.wilamowski.drecho.connectors.model.VisitModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
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

  public void initTable() {
    Set<VisitDto> visitSet = visitModel.listVisitsBy(0, 100);
    VisitVmMapper.toListToVM( visitSet );
    loadVisits(visitSet);
  }

  private void loadVisits(Set<VisitDto> visitSet) {
    if (visitSet != null) {
      logger.debug("SERVICE - visit service returns: {} entity items", visitSet.size());
      Set<VisitVM> patientsFxBean = VisitVmMapper.toListToVM(visitSet);
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
    visits.addAll( visitVMList );
  }

  public void fetchByPatient(PatientFx patientFx) {
    Set<VisitDto> visitSet = visitModel.listVisitsBy( PatientVmMapper.toDomain(patientFx));
    loadVisits(visitSet);
  }

  public void fetchByDate(LocalDate date) {
    Set<VisitDto> visitSet = visitModel.listVisitsBy(date);
    loadVisits(visitSet);
  }

  public void clearSearchResults() {
    visits.clear();
  }
}
