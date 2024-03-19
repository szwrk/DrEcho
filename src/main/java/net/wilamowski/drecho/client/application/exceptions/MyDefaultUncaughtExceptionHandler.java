package net.wilamowski.drecho.client.application.exceptions;

import net.wilamowski.drecho.client.presentation.customs.modals.NativeAlert;
import net.wilamowski.drecho.client.application.infra.util.screenshot.Screenshoter;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class MyDefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

  private static final Logger log = LogManager.getLogger(MyDefaultUncaughtExceptionHandler.class);

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    Thread.setDefaultUncaughtExceptionHandler(
        (thread, throwable) -> {
          String msg =
              String.format(
                  "Handler caught exception, thread: %1$s, msg: %2$s, stack: %3$s",
                  thread.getName(),
                  throwable.getMessage(),
                  ExceptionUtils.getStackTrace(throwable));
          log.error(msg);
          log.error(e.getMessage(), e);
          Screenshoter.shot();
          NativeAlert.safeError(e);
        });
  }
}
