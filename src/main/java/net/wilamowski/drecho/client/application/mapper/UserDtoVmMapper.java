package net.wilamowski.drecho.client.application.mapper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.wilamowski.drecho.client.presentation.user.UserVM;
import net.wilamowski.drecho.shared.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class UserDtoVmMapper {
  private static final Logger logger = LogManager.getLogger(UserDtoVmMapper.class);

  public static UserVM toVm(UserDto dto) {
    StringProperty loginProperty = new SimpleStringProperty(dto.login());
    StringProperty firstNameProperty = new SimpleStringProperty(dto.firstName());
    StringProperty surnameProperty = new SimpleStringProperty(dto.surname());
    StringProperty licenceNumberProperty = new SimpleStringProperty(dto.licenceNumber());
    return new UserVM(loginProperty, firstNameProperty, surnameProperty, licenceNumberProperty);
  }

  public static UserDto toDto(UserVM userVM) {
    logger.trace("[MAPPER-VM-DTO] Entering toDto...");
    logger.debug("[MAPPER-VM-DTO] Input: {}", userVM);
    logger.debug(userVM);
    UserDto newDto =
        UserDto.builder()
            .login(userVM.getLogin().getValue())
            .firstName(userVM.getName().getValue())
            .surname(userVM.getSurname().getValue())
            .licenceNumber(userVM.getLicenceNumber().getValue())
            .build();
    logger.debug("[MAPPER-VM-DTO] Output: {}", newDto);
    logger.trace("[MAPPER-VM-DTO] Exiting toDto...");
    return newDto;
  }
}
