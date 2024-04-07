package net.ape.modules.blatant;

import java.util.Arrays;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.MoveEvent;
import net.ape.events.MovementData;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Dropdown;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.modules.Module;
import net.minecraft.class_124;
import net.minecraft.class_1294;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class Speed extends Module {
   Dropdown mode = new Dropdown("Mode", this, Arrays.asList("Vanilla", "Vulcan", "Verus", "Constant"), null);
   Slider speedVal = new Slider("Speed", this, 1, 1000, 370, 1000.0);
   Slider jumpMotion = new Slider("Jump Motion", this, 1, 42, 42, 100.0);
   Toggle strafe = new Toggle("Strafe", this, null);
   Toggle waterCheck = new Toggle("Liquid Check", this, null);
   Toggle sprintMotion = new Toggle("Sprint Motion", this, null);
   private static class_243 ZERO = new class_243(0.0, 0.0, 0.0);
   double jumps = 0.0;

   public Speed() {
      super("Speed", ClickGui.blatant);
   }

   private class_243 getMoveDirection(double bps, boolean modified) {
      double velZ = (double)ApeClient.mc.field_1724.field_3913.field_3905;
      double velX = (double)ApeClient.mc.field_1724.field_3913.field_3907;
      return movementInputToVelocity(
         new class_243(velX, 0.0, velZ),
         bps,
         MovementData.event.movementCorrection && modified ? MovementData.event.yaw : ApeClient.mc.field_1724.method_36454()
      );
   }

   private class_243 getMoveDirection(double bps) {
      return this.getMoveDirection(bps, true);
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
   public void moveTick(MoveEvent event) {
      if (ApeClient.mc.field_1724 != null && ApeClient.mc.field_1724.field_3913 != null) {
         if (ApeClient.mc.field_1724.field_3913.field_3904) {
            return;
         }

         if (this.waterCheck.enabled && ApeClient.mc.field_1724.method_52535()) {
            return;
         }

         double speedAmount = (double)this.speedVal.value / 1000.0;
         double jumpVal = (double)((float)this.jumpMotion.value / 100.0F + ApeClient.mc.field_1724.method_37416());
         double currentSpeed = new class_243(event.motion.field_1352, 0.0, event.motion.field_1350).method_1033();
         String var9 = this.mode.value;
         class_243 moveDirection;
         switch (var9) {
            case "Vulcan":
               speedAmount = (double)ApeClient.mc.field_1724.field_3913.field_3905 != 0.0 && (double)ApeClient.mc.field_1724.field_3913.field_3907 != 0.0
                  ? 0.468F
                  : 0.479F;
               speedAmount += 0.12F;
               if (ApeClient.mc.field_1724.method_6059(class_1294.field_5904)) {
                  speedAmount += (double)((float)(ApeClient.mc.field_1724.method_6112(class_1294.field_5904).method_5578() + 1) * 0.072F);
               }

               moveDirection = this.getMoveDirection(ApeClient.mc.field_1724.field_36331 ? speedAmount : currentSpeed);
               if (event.motion.field_1351 < 0.16F && event.motion.field_1351 > -0.4F) {
                  event.motion = new class_243(
                     event.motion.field_1352,
                     event.motion.field_1351 == -0.14398374719323567 ? event.motion.field_1351 - 0.3F : event.motion.field_1351 - 0.15F,
                     event.motion.field_1350
                  );
               }
               break;
            case "Verus":
               double jumpSpeed = 0.55F;
               double constantSpeed = 0.376F;
               if (ApeClient.mc.field_1724.method_6059(class_1294.field_5904)) {
                  jumpSpeed += (double)((float)(ApeClient.mc.field_1724.method_6112(class_1294.field_5904).method_5578() + 1) * 0.055F);
                  constantSpeed += (double)((float)(ApeClient.mc.field_1724.method_6112(class_1294.field_5904).method_5578() + 1) * 0.055F);
               }

               jumpSpeed *= (double)(ApeClient.mc.field_1724.method_5624() ? 1.0F : 0.89F);
               constantSpeed *= (double)(ApeClient.mc.field_1724.method_5624() ? 1.0F : 0.89F);
               if (currentSpeed < 0.12 && ApeClient.mc.field_1724.field_36331) {
                  currentSpeed = 0.1F;
               } else {
                  currentSpeed = ApeClient.mc.field_1724.field_36331 ? jumpSpeed : Math.max(currentSpeed, constantSpeed);
               }

               moveDirection = this.getMoveDirection(currentSpeed);
               event.motion = event.motion
                  .method_1031(0.0, (event.motion.field_1351 > 0.0 ? -0.0784000015258789 : event.motion.field_1351) - event.motion.field_1351, 0.0);
               break;
            case "Constant":
               moveDirection = this.getMoveDirection(speedAmount);
               break;
            default:
               moveDirection = this.getMoveDirection(ApeClient.mc.field_1724.field_36331 ? currentSpeed + 0.2 : currentSpeed);
         }

         if (ApeClient.mc.field_1724.field_36331 && moveDirection != ZERO) {
            this.jumps++;
            event.motion = new class_243(moveDirection.field_1352, jumpVal, moveDirection.field_1350);
            return;
         }

         if (this.strafe.enabled || moveDirection.method_1033() < 0.01) {
            event.motion = new class_243(moveDirection.field_1352, event.motion.field_1351, moveDirection.field_1350);
         }
      }
   }

   @Override
   public String extra() {
      return class_124.field_1080 + " " + this.mode.value;
   }
}
