package net.wilamowski.drecho.client.presentation.customs.animations;

import atlantafx.base.util.Animations;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author Arkadiusz Wilamowski
 *     <p></><a href="https://github.com/szwrk">GitHub</a>
 *     <p>For questions or inquiries, at contact arek@wilamowski.net
 */
public class AnimationsUtil {

  public static final Duration MILLIS = Duration.millis(500);
  public static final int OFFSET = 5;

  public static Timeline animateDataRefresh(Node node) {
    return Animations.fadeIn(node, MILLIS);
  }

  public static Timeline userCallToActionAnimation(Node node) {
    return Animations.shakeX(node, OFFSET);
  }
}
