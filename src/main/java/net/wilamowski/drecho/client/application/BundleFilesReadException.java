package net.wilamowski.drecho.client.application;

import java.util.List;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class BundleFilesReadException extends Exception {

  private final List<String> wrongFileName;
  private final String message;

  public BundleFilesReadException(String message, List<String> wrongFileName) {
    this.wrongFileName = wrongFileName;
    this.message = message;
  }

  public List<String> getWrongFiles() {
    return wrongFileName;
  }
}
