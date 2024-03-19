package net.wilamowski.drecho.connectors.model.standalone.domain.user;

import java.util.Optional;
import net.wilamowski.drecho.connectors.model.UserModel;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService implements UserModel {
  private static final Logger logger = LogManager.getLogger( UserService.class);
  private final UserRepository userRepository;

  public UserService(UserRepository repository) {
    logger.debug("[SERVICE] Initializing UserService {} ...", repository.getClass().getSimpleName());
    this.userRepository = repository;
  }

  public void addUser(User user) {
    logger.debug("[SERVICE] Adding data... {}", user.getLogin());
    userRepository.addUser(user);
  }

  public Optional<User> getUserByLogin(String login) {
    logger.debug("[SERVICE] Finding data... {}", login);
    return userRepository.findByLogin(login);
  }
}
