package net.wilamowski.drecho.client.presentation.visit;

import net.wilamowski.drecho.client.GeneralUiException;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class VisitVmConfirmationException extends GeneralUiException {
  private static final String defaultHeaderKey = "e.014.header";
  private static final String defaultContentMsgKey = "e.014.msg";

  public VisitVmConfirmationException() {
    super( defaultHeaderKey , defaultContentMsgKey );
  }

  public VisitVmConfirmationException(String customHeaderKey,String customContentKey ) {
    super( customHeaderKey , customContentKey );
  }

}
