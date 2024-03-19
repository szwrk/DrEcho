package net.wilamowski.drecho.connectors.model.standalone.persistance.impl.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.connectors.model.standalone.persistance.UserRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRepositoryInMemory implements UserRepository {

    private static final Logger log = LogManager.getLogger(UserRepositoryInMemory.class);
    private final List<User> list = new ArrayList<>();

    private UserRepositoryInMemory() {
        User user1 = new User.Builder("ADM", "123").firstName("Jan").surname("Kowalski").specialization("Administrator").titlePrefix("inz.").build();
        User user2 = new User.Builder("LEK", "123").firstName("Anna").surname("Nowak").specialization("dermatologia").titlePrefix("lekarz").build();
        User user3 = new User.Builder("REJ", "123").firstName("Katarzyna").surname("Malinowska").specialization("rejestratorka").titlePrefix("rej.").build();
        user3.setBlocked();
        list.add(user1);
        list.add(user2);
        list.add(user3);
    }

    public static UserRepository getInstance(){
        return createUserRepositoryInMemory();
    }

    public static UserRepositoryInMemory createUserRepositoryInMemory() {
        return new UserRepositoryInMemory( );
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
        throw new NotImplementedException( "In-memory user repository is not implemented yet" );
    }
}
