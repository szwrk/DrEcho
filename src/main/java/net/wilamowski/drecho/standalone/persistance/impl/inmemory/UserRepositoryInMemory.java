package net.wilamowski.drecho.standalone.persistance.impl.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.wilamowski.drecho.standalone.domain.user.account.User;
import net.wilamowski.drecho.standalone.persistance.UserRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRepositoryInMemory implements UserRepository {

  private static final Logger log = LogManager.getLogger(UserRepositoryInMemory.class);
  private final List<User> list = new ArrayList<>();

  private UserRepositoryInMemory() {
    User user1 =
        User.builder()
            .login("ADM")
            .password("123")
            .firstName("Jan")
            .surname("Kowalski")
            .specialization("Administrator")
            .titlePrefix("inz.")
            .build();
    User user2 =
        User.builder()
            .login("ADM")
            .password("123")
            .firstName("Anna")
            .surname("Nowak")
            .specialization("dermatologia")
            .titlePrefix("lekarz")
            .build();
    User user3 =
        User.builder()
            .login("ADM")
            .password("123")
            .firstName("Katarzyna")
            .surname("Malinowska")
            .specialization("rejestratorka")
            .titlePrefix("rej.")
            .isBlocked(true)
            .build();
    list.add(user1);
    list.add(user2);
    list.add(user3);
  }

  public static UserRepository getInstance() {
    return createUserRepositoryInMemory();
  }

  public static UserRepositoryInMemory createUserRepositoryInMemory() {
    return new UserRepositoryInMemory();
  }

  @Override
  public Optional<User> findByLogin(String login) {
    log.traceEntry();
    Optional<User> user = list.stream().filter(u -> u.getLogin().equals(login)).findFirst();
    log.debug("[REPOSITORY] Finded user: {}", user);
    log.traceExit();
    return user;
  }

  @Override
  public Boolean isNonBlocked(String login) {
    return null;
  }

  @Override
  public void addUser(User user) {
    log.error("[REPOSITORY] Not impl yet");
    throw new NotImplementedException("In-memory user repository is not implemented yet");
  }
}
