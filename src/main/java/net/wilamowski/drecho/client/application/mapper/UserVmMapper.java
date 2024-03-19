package net.wilamowski.drecho.client.application.mapper;

import javafx.beans.property.SimpleStringProperty;
import net.wilamowski.drecho.connectors.model.standalone.domain.user.account.User;
import net.wilamowski.drecho.client.presentation.user.UserVM;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class UserVmMapper {
  public static UserVM of(User user) {
    UserVM userVM =
        new UserVM(
            new SimpleStringProperty(user.getLogin()),
            new SimpleStringProperty(user.getFirstName()),
            new SimpleStringProperty(user.getSurname()),
            new SimpleStringProperty(user.getLicenceNumber()));
    return userVM;
  }
}
