package net.wilamowski.drecho.connectors.model.standalone.persistance;

import java.util.Optional;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;

public interface UserRepository {
  Optional<User> findByLogin(String login);

  Boolean isNonBlocked(String login);

  void addUser(User user);
}
