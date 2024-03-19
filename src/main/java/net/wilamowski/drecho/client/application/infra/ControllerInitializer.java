package net.wilamowski.drecho.client.application.infra;
/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public interface ControllerInitializer {
  void initControllers(Object controller, GeneralViewHandler viewHandler);
}
