package net.ape.modules.combat;

import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.GameTickEvent;
import net.ape.gui.ClickGui;
import net.ape.handlers.NotificationHandler;
import net.ape.modules.Module;
import net.minecraft.class_304;
import org.lwjgl.glfw.GLFW;

public class WTap extends Module {
   public boolean sprintHit = false;
   boolean modified = false;

   public WTap() {
      super("WTap", ClickGui.combat);
   }

   @EventListener
   public void worldTick(GameTickEvent event) {
      if (ApeClient.mc.field_1724 != null && ApeClient.mc.field_1687 != null) {
         if (this.sprintHit) {
            NotificationHandler.addNotification("WTap", "wtapped", 1.0);
            this.sprintHit = false;
            this.modified = true;
            class_304.method_1416(ApeClient.mc.field_1690.field_1894.method_1429(), false);
         } else if (this.modified) {
            class_304.method_1416(
               ApeClient.mc.field_1690.field_1894.method_1429(),
               GLFW.glfwGetKey(ApeClient.mc.method_22683().method_4490(), ApeClient.mc.field_1690.field_1894.method_1429().method_1444()) == 1
            );
            this.modified = false;
         }
      }
   }
}
