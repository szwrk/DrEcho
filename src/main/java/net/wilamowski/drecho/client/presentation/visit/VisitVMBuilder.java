package net.wilamowski.drecho.client.presentation.visit;

import javafx.beans.property.ObjectProperty;
import net.wilamowski.drecho.client.presentation.patients.PatientFx;
import net.wilamowski.drecho.client.presentation.user.UserVM;

import java.time.LocalDateTime;

public class VisitVMBuilder{
    private ObjectProperty<LocalDateTime> realizationDateTimeProperty;
    private ObjectProperty<LocalDateTime> viewStartDateTimeProperty;
    private ObjectProperty<UserVM> selectedRegistrant;
    private ObjectProperty<UserVM> selectedPerformer;
    private ObjectProperty<PatientFx> selectedPatient;

    public VisitVMBuilder setRealizationDateTimeProperty(ObjectProperty<LocalDateTime> realizationDateTimeProperty) {
        this.realizationDateTimeProperty = realizationDateTimeProperty;
        return this;
    }

    public VisitVMBuilder setViewStartDateTimeProperty(ObjectProperty<LocalDateTime> viewStartDateTimeProperty) {
        this.viewStartDateTimeProperty = viewStartDateTimeProperty;
        return this;
    }

    public VisitVMBuilder setSelectedRegistrant(ObjectProperty<UserVM> selectedRegistrant) {
        this.selectedRegistrant = selectedRegistrant;
        return this;
    }

    public VisitVMBuilder setSelectedPerformer(ObjectProperty<UserVM> selectedPerformer) {
        this.selectedPerformer = selectedPerformer;
        return this;
    }

    public VisitVMBuilder setSelectedPatient(ObjectProperty<PatientFx> selectedPatient) {
        this.selectedPatient = selectedPatient;
        return this;
    }

    public VisitVM createVisitVM() {
        return new VisitVM( realizationDateTimeProperty , viewStartDateTimeProperty , selectedRegistrant , selectedPerformer , selectedPatient );
    }
}