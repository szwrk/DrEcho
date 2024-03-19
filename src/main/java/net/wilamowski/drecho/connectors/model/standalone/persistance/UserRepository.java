package net.wilamowski.drecho.connectors.model.standalone.persistance;

import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByLogin(String login);

  Boolean isNonBlocked(String login);

  void addUser(User user);
}
