package net.ape.modules;

import java.util.Comparator;
import net.ape.gui.components.clickgui.ModuleButton;
import net.ape.gui.components.clickgui.Window;

public class Module {
   public String name;
   public int key = -99;
   public boolean enabled;
   public ModuleButton button;

   public Module(String name, Window wind) {
      this.name = name;
      this.button = new ModuleButton(name, this);
      wind.objects.add(this.button);
      wind.objects.sort(Comparator.comparing(o -> ((ModuleButton)o).text));
   }

   public void toggle() {
      this.enabled = !this.enabled;
      if (this.enabled) {
         this.onEnable();
      } else {
         this.onDisable();
      }
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public String extra() {
      return "";
   }
}
