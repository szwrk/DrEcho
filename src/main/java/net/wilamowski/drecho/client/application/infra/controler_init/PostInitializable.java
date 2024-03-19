package net.wilamowski.drecho.client.application.infra.controler_init;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net The {@code PostInitializable}
 *     interface should be implemented by controllers that require additional initialization steps
 *     to be performed after the necessary objects have been initialized, such as loading a scene.
 */
public interface PostInitializable {
  void postInitialize();
}
