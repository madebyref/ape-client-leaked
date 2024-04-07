package net.ape.handlers;

import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.ape.gui.components.Notification;

public class NotificationHandler {
   public static void addNotification(String title, String text, double duration) {
      if (ClickGui.pane5.notif.enabled) {
         ApeClient.notifis.add(new Notification(title, text, duration, "info"));
      }
   }

   public static void addNotification(String title, String text, String type, double duration) {
      if (ClickGui.pane5.notif.enabled) {
         ApeClient.notifis.add(new Notification(title, text, duration, type));
      }
   }
}
