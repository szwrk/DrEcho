package net.wilamowski.drecho.standalone.service.authenticator;

public class Credentials {
  private final String login;
  private final String password;

  public Credentials(String login, String password) {
    this.login = login;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public String getLogin() {
    return login;
  }
}
