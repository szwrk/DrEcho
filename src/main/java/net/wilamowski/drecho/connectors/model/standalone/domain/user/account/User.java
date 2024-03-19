package net.wilamowski.drecho.connectors.model.standalone.domain.user.account;

import lombok.ToString;

@ToString
public class User {
  private final String login;
  private final String password;
  private String firstName;
  private String surname;
  private String titlePrefix;
  private String specialization; // specialization, function...
  private String licenceNumber;
  private Boolean isBlocked;

  private User( String login, String password) {
      this.login = login;
    this.password = password;
  }

  private User( String login, String password, String firstName, String surname) {

      this.login = login;
    this.password = password;
    this.firstName = firstName;
    this.surname = surname;
    this.isBlocked = false;
  }

  public String getLogin() {
    return login;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getSurname() {
    return surname;
  }

  public String getTitlePrefix() {
    return titlePrefix;
  }

  void setTitlePrefix(String titlePrefix) {
    this.titlePrefix = titlePrefix;
  }

  public String getSpecialization() {
    return specialization;
  }

  void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public String getPassword() {
    return password;
  }

  public Boolean isBlocked() {
    return isBlocked;
  }

  public void setBlocked() {
    isBlocked = true;
  }

  public String getLicenceNumber() {
    return licenceNumber;
  }

  public void setLicenceNumber(String licenceNumber) {
    this.licenceNumber = licenceNumber;
  }

  public static class Builder {
    private String login;
    private String password;
    private String firstName;
    private String surname;
    private String titlePrefix;
    private String specialization;

    public Builder(String login, String password) {
      this.login = login;
      this.password = password;
    }

    public Builder login(String login) {
      this.login = login;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder surname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder titlePrefix(String titlePrefix) {
      this.titlePrefix = titlePrefix;
      return this;
    }

    public Builder specialization(String specialization) {
      this.specialization = specialization;
      return this;
    }

    public User build() {
      User user = new User(this.login, password);
      user.firstName = this.firstName;
      user.surname = this.surname;
      user.titlePrefix = this.titlePrefix;
      user.specialization = this.specialization;
      return user;
    }
  }
}
