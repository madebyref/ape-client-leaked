package net.ape.gui.components.clickgui.settings;

import net.ape.gui.components.clickgui.SettingsPane;
import net.ape.gui.components.clickgui.Slider;

public class GuiPane extends SettingsPane {
   public Slider rainbowSpeed = new Slider("Rainbow speed", this, 1, 100, 10, 10.0);

   public GuiPane(SettingsPane pane) {
      super("GUI", pane);
   }
}
