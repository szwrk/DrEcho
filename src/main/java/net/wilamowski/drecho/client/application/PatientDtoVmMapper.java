//package net.wilamowski.drecho.client.application;
///*Author:
//Arkadiusz Wilamowski
//https://github.com/szwrk
//*/
//
//import java.time.LocalDate;
//import javafx.beans.property.*;
//import net.wilamowski.drecho.client.presentation.patients.PatientVM;
//import net.wilamowski.drecho.shared.dto.PatientDto;
//
//public class PatientDtoVmMapper {
//    public static PatientVM toVM(PatientDto patientDto) {
//        LongProperty              idProperty                 = new SimpleLongProperty(patientDto.getId());
//        StringProperty            nameProperty               = new SimpleStringProperty(patientDto.getName());
//        StringProperty            lastNameProperty           = new SimpleStringProperty(patientDto.getLastName());
//        StringProperty            peselProperty              = new SimpleStringProperty(patientDto.getPesel());
//        StringProperty            nameOfCityBirthProperty    = new SimpleStringProperty(patientDto.getNameOfCityBirth());
//        StringProperty            codeOfCityBirthProperty    = new SimpleStringProperty(patientDto.getCodeOfCityBirth());
//        ObjectProperty<LocalDate> dateBirthProperty          = new SimpleObjectProperty<>(patientDto.getDateBirth());
//        StringProperty            generalPatientNoteProperty = new SimpleStringProperty(patientDto.getGeneralPatientNote());
//        StringProperty patientTelephoneNumberProperty = new SimpleStringProperty(patientDto.getPatientTelephoneNumber());
//
//        return PatientVM.builder()
//                .id(idProperty)
//                .name(nameProperty)
//                .lastName(lastNameProperty)
//                .pesel(peselProperty)
//                .nameOfCityBirth(nameOfCityBirthProperty)
//                .codeOfCityBirth(codeOfCityBirthProperty)
//                .dateBirth(dateBirthProperty)
//                .generalPatientNote(generalPatientNoteProperty)
//                .patientTelephoneNumber(patientTelephoneNumberProperty)
//                .build();
//    }
//
//    public static PatientDto toDto(PatientVM patientVM) {
//        return PatientDto.builder()
//                .id(patientVM.getId().get())
//                .name(patientVM.getName().get())
//                .lastName(patientVM.getLastName().get())
//                .pesel(patientVM.getPesel().get())
//                .nameOfCityBirth(patientVM.getNameOfCityBirth().get())
//                .codeOfCityBirth(patientVM.getCodeOfCityBirth().get())
//                .dateBirth(patientVM.getDateBirth().get())
//                .generalPatientNote(patientVM.getGeneralPatientNote().get())
//                .patientTelephoneNumber(patientVM.getPatientTelephoneNumber().get())
//                .build();
//    }
//
//}
