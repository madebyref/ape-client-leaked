package net.ape.gui.components.clickgui.settings;

import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.SettingsButton;
import net.ape.gui.components.clickgui.SettingsPane;

public class MainPane extends SettingsPane {
   public GUIColorSlider guiTheme;

   public MainPane() {
      super("Settings", null);
      this.objects.add(new SettingsButton("General", callback -> {
         this.visible = false;
         ClickGui.pane2.visible = true;
      }));
      this.objects.add(new SettingsButton("Modules", callback -> {
         this.visible = false;
         ClickGui.pane3.visible = true;
      }));
      this.objects.add(new SettingsButton("GUI", callback -> {
         this.visible = false;
         ClickGui.pane4.visible = true;
      }));
      this.objects.add(new SettingsButton("Notifications", callback -> {
         this.visible = false;
         ClickGui.pane5.visible = true;
      }));
      this.guiTheme = new GUIColorSlider("GUI Theme", this);
   }
}
