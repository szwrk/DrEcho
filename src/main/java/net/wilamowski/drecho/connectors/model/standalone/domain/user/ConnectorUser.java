package net.wilamowski.drecho.connectors.model.standalone.domain.user;

import java.util.Optional;
import net.wilamowski.drecho.connectors.model.UserModel;
import net.wilamowski.drecho.connectors.model.mapper.UserDomainDtoMapper;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import net.wilamowski.drecho.shared.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectorUser implements UserModel {
  private static final Logger logger = LogManager.getLogger( ConnectorUser.class);
  private final UserRepository userRepository;


  public ConnectorUser(UserRepository repository) {
    logger.debug(
        "[SERVICE] Initializing UserService {} ...", repository.getClass().getSimpleName());
    this.userRepository = repository;
  }
  @Override
  public void addUser(UserDto dto) {
    logger.debug("[SERVICE] Adding data... {}", dto.login());
    User user = UserDomainDtoMapper.toDomain( dto );
    userRepository.addUser(user);
  }
  @Override
  public Optional<UserDto> findUserByLogin(String login) {
    logger.debug("[SERVICE] Finding user by login: {}", login);

    if (login == null || login.isEmpty()) {
      logger.error("[SERVICE] Login cannot be null or empty");
      return Optional.empty();
    }

    Optional<User> byLogin = userRepository.findByLogin(login);

    if (byLogin.isPresent()) {
      UserDto dto = UserDomainDtoMapper.toDto(byLogin.get());
      logger.debug("[SERVICE] User found for login '{}' - {}", login, dto);
      return Optional.ofNullable(dto);
    } else {
      logger.debug("[SERVICE] No user found for login '{}'", login);
      return Optional.empty();
    }
  }

  public Optional<User> findDomainUserByLogin(String login) {
    logger.debug("[SERVICE] Finding user by login: {}", login);
    if (login == null || login.isEmpty()) {
      logger.error("[SERVICE] Login cannot be null or empty");
      return Optional.empty();
    }
    return userRepository.findByLogin(login);
  }
}
