package net.wilamowski.drecho.client.presentation.complex.visits;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.ToString;
import net.wilamowski.drecho.app.dto.PatientDto;
import net.wilamowski.drecho.app.dto.VisitDtoDetailedQuery;
import net.wilamowski.drecho.client.application.mapper.PatientDtoVmMapper;
import net.wilamowski.drecho.client.application.mapper.VisitDtoVmMapper;
import net.wilamowski.drecho.client.presentation.patients.PatientVM;
import net.wilamowski.drecho.client.presentation.visit.VisitVM;
import net.wilamowski.drecho.configuration.backend_ports.PatientService;
import net.wilamowski.drecho.configuration.backend_ports.VisitService;
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

  private final VisitService visitService;
  private final ObservableList<VisitVM> visits = FXCollections.observableArrayList();
  private final PatientService patientService;

  public VisitDashboardViewModel(VisitService visitService, PatientService patientService) {
    this.visitService = visitService;
    this.patientService = patientService;
  }

  public void searchByPatient(PatientVM patientVM , int page) {
    logger.trace("Clicked search by selected patient");
    if ( patientVM != null) {
      PatientDto                 patient  = PatientDtoVmMapper.toDto( patientVM );
      Set<VisitDtoDetailedQuery> visitSet = visitService.listVisitsBy(patient, page);
      loadVisitsToTable(visitSet);
    }
  }

  private void loadVisitsToTable(Set<VisitDtoDetailedQuery> visitSet) {
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
    Set<VisitDtoDetailedQuery> visitSet = visitService.listVisitsBy(date, page);
    loadVisitsToTable(visitSet);
  }

  public void clearSearchResults() {
    visits.clear();
  }

  public int checkIsPatientExist(String par) {
    return patientService.counterByCitizenCode(par);
  }

  public List<PatientDto> findPatientByCitizenCode(String par) {
    return patientService.findByCitizenCode(  par, 0 );
  }


  public PatientVM findByCitizenCodeFirstRecord(String par) {
    PatientDto byCitizenCodeFirstRecord = patientService.findByCitizenCodeFirstRecord( par );
    return PatientDtoVmMapper.toVm( byCitizenCodeFirstRecord );
  }
}
