package net.wilamowski.drecho.client.application.infra;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */

@Deprecated
public interface ControllerInitializer {
  void initController(Object controller, GeneralViewHandler viewHandler);
}
