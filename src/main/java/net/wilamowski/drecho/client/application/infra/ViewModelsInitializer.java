package net.wilamowski.drecho.client.application.infra;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 * Initializes the state of the controller's view models. This method can be used to set up the
 * initial state for the associated view models used by the controller.
 */
public interface ViewModelsInitializer {
  void initializeViewModels(ViewModels factory);
}
