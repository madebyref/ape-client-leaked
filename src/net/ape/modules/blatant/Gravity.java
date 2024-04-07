package net.ape.modules.blatant;

import java.util.Arrays;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Dropdown;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.modules.Module;

public class Gravity extends Module {
   public Dropdown mode = new Dropdown("Mode", this, Arrays.asList("Normal", "Verus", "Vulcan"), value -> this.gravity.visible = value.equals("Normal"));
   public Slider gravity = new Slider("Gravity", this, 0, 80, 80, 1000.0);
   public Toggle fall = new Toggle("Only while falling", this, null);

   public Gravity() {
      super("Gravity", ClickGui.world);
   }
}
