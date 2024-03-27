package net.wilamowski.drecho.client.application.mapper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.wilamowski.drecho.shared.dto.UserDto;
import net.wilamowski.drecho.client.presentation.user.UserVM;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class UserDtoVmMapper {

  public static UserVM toVm(UserDto dto) {
    StringProperty loginProperty = new SimpleStringProperty(dto.getLogin());
    StringProperty firstNameProperty = new SimpleStringProperty(dto.getFirstName());
    StringProperty surnameProperty = new SimpleStringProperty(dto.getSurname());
    StringProperty licenceNumberProperty = new SimpleStringProperty(dto.getLicenceNumber());

    return new UserVM(
            loginProperty,
            firstNameProperty,
            surnameProperty,
            licenceNumberProperty
    );
  }
  public static UserDto toDto(UserVM userVM) {
    return UserDto.builder()
            .login(userVM.getLogin().getValue())
            .firstName(userVM.getName().getValue())
            .surname(userVM.getSurname().getValue())
            .licenceNumber(userVM.getLicenceNumber().getValue())
            .build();
  }
}
