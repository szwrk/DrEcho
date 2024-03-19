package net.wilamowski.drecho.client.presentation.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
@ToString
@Getter
public class UserVM {
  public static final String FULL_NAME_CONCATENATE_STR_FORMAT = "%s %s";
  private final StringProperty login;
  private final StringProperty name;
  private final StringProperty surname;
  private final StringProperty licenceNumber;
  private final SimpleStringProperty fullName;

  public UserVM(
      StringProperty login,
      StringProperty name,
      StringProperty surname,
      StringProperty licenceNumber) {
    this.login = login;
    this.name = name;
    this.surname = surname;
    this.licenceNumber = licenceNumber;
    this.fullName =
        new SimpleStringProperty(
            (surname.get() != null ? surname.get() : " - ")
                + " "
                + (name.get() != null ? name.get() : " - ")
                + " "
                + (licenceNumber.get() != null ? licenceNumber.get() : " - "));
  }
}
