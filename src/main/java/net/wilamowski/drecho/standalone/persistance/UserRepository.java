package net.wilamowski.drecho.standalone.persistance;

import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.user.account.User;

public interface UserRepository {
  Optional<User> findByLogin(String login);

  Boolean isNonBlocked(String login);

  void addUser(User user);
}
