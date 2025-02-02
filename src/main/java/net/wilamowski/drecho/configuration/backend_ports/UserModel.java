package net.wilamowski.drecho.configuration.backend_ports;

import java.util.Optional;
import net.wilamowski.drecho.app.dto.UserDto;

public interface UserModel {

    void addUser(UserDto dto);

    Optional<UserDto> findUserByLogin(String login);
}
