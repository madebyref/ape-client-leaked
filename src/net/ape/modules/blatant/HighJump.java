package net.ape.modules.blatant;

import java.util.Arrays;
import net.ape.ApeClient;
import net.ape.events.CollisionEvent;
import net.ape.events.EventListener;
import net.ape.events.MoveEvent;
import net.ape.events.MovementData;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Dropdown;
import net.ape.modules.Module;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_3532;

public class HighJump extends Module {
   public Dropdown mode = new Dropdown("Mode", this, Arrays.asList("Normal", "Vulcan", "Verus", "Karhu", "Collide"), null);
   int enabledTicks = 0;

   public HighJump() {
      super("HighJump", ClickGui.blatant);
   }

   @Override
   public void onEnable() {
      this.enabledTicks = 19;
   }

   @EventListener
   public void CollisionTick(CollisionEvent event) {
      if (this.mode.value.equals("Collide")) {
         class_238 playerBox = ApeClient.mc.field_1724.method_5829();
         event.list.add(class_259.method_1078(playerBox.method_989(0.0, playerBox.field_1322 - playerBox.field_1325, 0.0)));
      }
   }

   @EventListener
   public void moveTick(MoveEvent event) {
      String var2 = this.mode.value;
      switch (var2) {
         case "Vulcan":
            if (event.motion.field_1351 < 0.0 && this.enabledTicks == 19) {
               this.enabledTicks = 19;
               return;
            }

            if (ApeClient.mc.field_1724.field_6017 < 0.1F && this.enabledTicks >= 18) {
               this.enabledTicks = 18;
               return;
            }

            this.enabledTicks--;
            if (this.enabledTicks % 6 == 5) {
               event.motion = new class_243(event.motion.field_1352, 9.5, event.motion.field_1350);
               return;
            }

            if (this.enabledTicks % 6 == 4) {
               event.motion = new class_243(event.motion.field_1352, -0.1F, event.motion.field_1350);
               return;
            }

            if (this.enabledTicks <= 0) {
               this.toggle();
            }
            break;
         case "Verus":
            double jumpMotion = (double)(0.42F + ApeClient.mc.field_1724.method_37416());
            event.motion = new class_243(
               event.motion.field_1352, event.motion.field_1351 < 0.3F ? jumpMotion : event.motion.field_1351, event.motion.field_1350
            );
            break;
         case "Karhu":
            if (this.enabledTicks > 10) {
               this.enabledTicks = 0;
            }

            if (!ApeClient.mc.field_1724.field_36331 && this.enabledTicks == 0) {
               this.enabledTicks = 0;
               return;
            }

            if (event.motion.field_1351 < 0.08F) {
               this.enabledTicks++;
            }

            if (this.enabledTicks > 6) {
               this.toggle();
            }

            event.motion = new class_243(
               event.motion.field_1352,
               event.motion.field_1351 < 0.08F ? (double)(0.42F + ApeClient.mc.field_1724.method_37416()) : event.motion.field_1351,
               event.motion.field_1350
            );
            break;
         case "Collide":
            if (ApeClient.mc.field_1724.field_36331) {
               event.motion = new class_243(event.motion.field_1352, (double)(0.42F + ApeClient.mc.field_1724.method_37416()), event.motion.field_1350);
               if (ApeClient.mc.field_1724.method_5624()) {
                  float f = (MovementData.event.movementCorrection ? MovementData.event.yaw : ApeClient.mc.field_1724.method_36454())
                     * (float) (Math.PI / 180.0);
                  event.motion = event.motion.method_1031((double)(-class_3532.method_15374(f) * 0.2F), 0.0, (double)(class_3532.method_15362(f) * 0.2F));
               }
            }
            break;
         default:
            event.motion = new class_243(event.motion.field_1352, 1.0, event.motion.field_1350);
      }
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (this.mode.value.equals("Vulcan") && ApeClient.mc.field_1724 != null && ApeClient.mc.field_1724.method_18798().field_1351 > 1.0) {
         ApeClient.mc.field_1724.method_5750(ApeClient.mc.field_1724.method_18798().field_1352, -0.1F, ApeClient.mc.field_1724.method_18798().field_1350);
      }
   }
}
