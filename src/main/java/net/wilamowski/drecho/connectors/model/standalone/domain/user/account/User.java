package net.wilamowski.drecho.connectors.model.standalone.domain.user.account;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class User {
  private final String login;
  private final String password;
  private String firstName;
  private String surname;
  private String titlePrefix;
  private String specialization; // specialization, function...
  private String licenceNumber;
  private Boolean isBlocked;
}
