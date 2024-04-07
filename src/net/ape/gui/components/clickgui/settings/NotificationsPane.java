package net.ape.gui.components.clickgui.settings;

import net.ape.gui.components.clickgui.SettingsPane;
import net.ape.gui.components.clickgui.Toggle;

public class NotificationsPane extends SettingsPane {
   public Toggle notif = new Toggle("Notifications", this, callback -> {
      if (this.togglenotif != null) {
         this.togglenotif.visible = (Boolean)callback;
      }
   }, true);
   public Toggle togglenotif = new Toggle("Toggle Alert", this, null, true);

   public NotificationsPane(SettingsPane pane) {
      super("Notifications", pane);
   }
}
