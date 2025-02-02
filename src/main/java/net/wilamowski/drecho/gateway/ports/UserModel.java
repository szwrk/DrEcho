package net.wilamowski.drecho.gateway.ports;

import java.util.Optional;
import net.wilamowski.drecho.app.dto.UserDto;

public interface UserModel {

    void addUser(UserDto dto);

    Optional<UserDto> findUserByLogin(String login);
}
