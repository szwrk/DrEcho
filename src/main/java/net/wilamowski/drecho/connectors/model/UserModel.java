package net.wilamowski.drecho.connectors.model;

import java.util.Optional;
import net.wilamowski.drecho.shared.dto.UserDto;

public interface UserModel {

    void addUser(UserDto dto);

    Optional<UserDto> findUserByLogin(String login);
}
