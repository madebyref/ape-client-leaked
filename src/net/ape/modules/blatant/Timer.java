package net.ape.modules.blatant;

import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Slider;
import net.ape.modules.Module;

public class Timer extends Module {
   public Slider timer = new Slider("Timer", this, 1, 20, 10, 10.0);

   public Timer() {
      super("Timer", ClickGui.blatant);
   }
}
