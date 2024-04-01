package net.wilamowski.drecho.connectors.model.mapper;

/*Author:
Arkadiusz Wilamowski
https://github.com/szwrk
*/

import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.shared.dto.UserDto;

public class UserDomainDtoMapper {


  public static UserDto toDto(User user) {
    return UserDto.builder()
            .login(user.getLogin())
            .password(user.getPassword())
            .firstName(user.getFirstName())
            .surname(user.getSurname())
            .titlePrefix(user.getTitlePrefix())
            .specialization(user.getSpecialization())
            .licenceNumber(user.getLicenceNumber())
            .isBlocked(user.getIsBlocked())
            .build();
  }

  public static User toDomain(UserDto userDto) {
    return User.builder()
            .login(userDto.login())
            .password(userDto.password())
            .firstName(userDto.firstName())
            .surname(userDto.surname())
            .titlePrefix(userDto.titlePrefix())
            .specialization(userDto.specialization())
            .licenceNumber(userDto.licenceNumber())
            .isBlocked(userDto.isBlocked())
            .build();
  }
}
