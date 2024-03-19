package net.wilamowski.drecho.client.presentation.debugger;

import javafx.scene.Parent;

/** Initializes the key combination event */
public interface DebugHandler {
  void initNode(Parent root);

  void watch(Object... objects);
}
