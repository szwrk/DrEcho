package net.wilamowski.drecho.client.presentation.customs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class ImageViewUtil {

  public static ImageView getImg(String imageShortName) {
    String imagePath = String.format("/assets/%s.png", imageShortName);
    return new ImageView(new Image(ImageViewUtil.class.getResource(imagePath).toExternalForm()));
  }
}
