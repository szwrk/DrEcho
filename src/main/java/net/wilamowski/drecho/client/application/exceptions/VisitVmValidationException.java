package net.wilamowski.drecho.client.application.exceptions;
/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 *     VisitVmValidationException - general exception for just alert&popup. If you need some specif handling, use dedicated exception.
 */
public class VisitVmValidationException extends GeneralUiException {
  private static final String defaultHeaderKey = "e.014.header";
  private static final String defaultContentMsgKey = "e.014.msg";

  public VisitVmValidationException() {
    super( defaultHeaderKey , defaultContentMsgKey );
  }
  public VisitVmValidationException(String customHeaderKey, String customContentKey ) {
    super( customHeaderKey , customContentKey );
  }
}
