package net.ape.modules.blatant;

import java.util.Arrays;
import net.ape.ApeClient;
import net.ape.events.CollisionEvent;
import net.ape.events.EventListener;
import net.ape.events.MoveEvent;
import net.ape.events.MovementData;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Dropdown;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.modules.Module;
import net.minecraft.class_124;
import net.minecraft.class_238;
import net.minecraft.class_2399;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_2680;
import net.minecraft.class_3532;

public class Fly extends Module {
   Dropdown mode = new Dropdown("Mode", this, Arrays.asList("Normal", "Collide", "CollideJump", "Sparky"), value -> {
      this.speedVal.visible = value.equals("Normal") || value.equals("Sparky");
      this.verticalSpeedVal.visible = this.speedVal.visible;
      this.vanillaKickBypass.visible = this.speedVal.visible;
   });
   Slider speedVal = new Slider("Speed", this, 1, 4000, 370, 1000.0);
   Slider verticalSpeedVal = new Slider("Vertical Speed", this, 1, 1000, 370, 1000.0);
   Toggle vanillaKickBypass = new Toggle("Vanilla Kick", this, null);
   private static class_243 ZERO = new class_243(0.0, 0.0, 0.0);
   double startHeight;
   double sparky;

   public Fly() {
      super("Fly", ClickGui.blatant);
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.startHeight = ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_5829().field_1322 : 0.0;
      this.sparky = 0.0;
   }

   private class_243 getMoveDirection(double bps) {
      double velZ = (double)ApeClient.mc.field_1724.field_3913.field_3905;
      double velX = (double)ApeClient.mc.field_1724.field_3913.field_3907;
      return movementInputToVelocity(
         new class_243(velX, 0.0, velZ), bps, MovementData.event.movementCorrection ? MovementData.event.yaw : ApeClient.mc.field_1724.method_36454()
      );
   }

   private static class_243 movementInputToVelocity(class_243 movementInput, double speed, float yaw) {
      double d = movementInput.method_1033();
      if (d < 1.0E-7) {
         return ZERO;
      } else {
         movementInput = movementInput.method_1029();
         if (movementInput.method_1033() < 1.0) {
            movementInput = movementInput.method_1021(0.998);
         }

         float f = class_3532.method_15374(yaw * (float) Math.PI / 180.0F) * (float)speed;
         float g = class_3532.method_15362(yaw * (float) Math.PI / 180.0F) * (float)speed;
         return new class_243(
            movementInput.field_1352 * (double)g - movementInput.field_1350 * (double)f,
            movementInput.field_1351,
            movementInput.field_1350 * (double)g + movementInput.field_1352 * (double)f
         );
      }
   }

   @EventListener
   public void CollisionTick(CollisionEvent event) {
      class_238 playerBox = ApeClient.mc.field_1724.method_5829();
      String var3 = this.mode.value;
      switch (var3) {
         case "CollideJump":
            event.list.add(class_259.method_1078(playerBox.method_989(0.0, this.startHeight - playerBox.field_1325, 0.0)));
            break;
         case "Collide":
            event.list
               .add(
                  class_259.method_1078(
                     playerBox.method_989(
                        0.0, playerBox.field_1322 - playerBox.field_1325 - (double)(ApeClient.mc.field_1724.field_3913.field_3903 ? 1 : 0), 0.0
                     )
                  )
               );
      }
   }

   @EventListener
   public void moveTick(MoveEvent event) {
      if (ApeClient.mc.field_1724 != null && ApeClient.mc.field_1724.field_3913 != null) {
         String var2 = this.mode.value;
         switch (var2) {
            case "CollideJump":
               if (ApeClient.mc.field_1724.field_36331) {
                  event.motion = new class_243(event.motion.field_1352, (double)(0.42F + ApeClient.mc.field_1724.method_37416()), event.motion.field_1350);
                  if (ApeClient.mc.field_1724.method_5624()) {
                     float f = (MovementData.event.movementCorrection ? MovementData.event.yaw : ApeClient.mc.field_1724.method_36454())
                        * (float) (Math.PI / 180.0);
                     event.motion = event.motion.method_1031((double)(-class_3532.method_15374(f) * 0.2F), 0.0, (double)(class_3532.method_15362(f) * 0.2F));
                  }
               }
            case "Collide":
               break;
            case "Sparky":
               class_2680 bruh = ApeClient.mc.field_1724.method_36601();
               if (bruh != null && bruh.method_26204() instanceof class_2399) {
                  this.sparky = 20.0;
               }

               if (this.sparky == 1.0) {
                  event.motion = new class_243(0.0, 0.0, 0.0);
               }

               this.sparky--;
               if (this.sparky <= 0.0) {
                  return;
               }
            default:
               class_243 moveDirection = this.getMoveDirection((double)this.speedVal.value / 1000.0);
               double verticalSpeed = (double)this.verticalSpeedVal.value / 1000.0;
               event.motion = new class_243(
                  moveDirection.field_1352,
                  (this.vanillaKickBypass.enabled ? -0.032 : 0.0)
                     + (ApeClient.mc.field_1724.field_3913.field_3904 ? verticalSpeed : (ApeClient.mc.field_1724.field_3913.field_3903 ? -verticalSpeed : 0.0)),
                  moveDirection.field_1350
               );
         }
      }
   }

   @Override
   public String extra() {
      return class_124.field_1080 + " " + this.mode.value;
   }
}
