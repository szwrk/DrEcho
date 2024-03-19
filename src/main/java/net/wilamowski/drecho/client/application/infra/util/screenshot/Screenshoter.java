package net.wilamowski.drecho.client.application.infra.util.screenshot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import net.wilamowski.drecho.client.presentation.customs.modals.NativeAlert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arkadiusz Wilamowski
 * <p></><a href="https://github.com/szwrk">GitHub</a></p>
 * <p> For questions or inquiries, at contact arek@wilamowski.net </p>
 */
public class Screenshoter {
  private static final Logger log = LogManager.getLogger(Screenshoter.class);

  public static void shot() {
    log.traceEntry();
    try {
      Robot robot = new Robot();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Rectangle screenSize = new Rectangle(toolkit.getScreenSize());
      BufferedImage screenshot = robot.createScreenCapture(screenSize);

      String fileName = genereteFileName();
      File outputFile = new File(fileName);
      ImageIO.write(screenshot, "png", outputFile);

      log.debug("Screenshot {} saved to: {}", fileName, outputFile.getAbsolutePath());
    } catch (AWTException | IOException | RuntimeException e) {
      log.error(e.getMessage(), e);
      NativeAlert.safeError(e);
    } finally {
      log.traceExit();
    }
  }

  private static String genereteFileName() {
    String dirPath = "screenshot";
    String baseName = "ss";
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    String formattedDateTime = now.format(formatter);

    File directory = new File(dirPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return dirPath + baseName + "_" + formattedDateTime + ".png";
  }
}
