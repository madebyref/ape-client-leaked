package net.ape.mixins;

import com.google.common.collect.Lists;
import java.util.List;
import net.ape.ApeClient;
import net.ape.events.MovementData;
import net.minecraft.class_3532;
import net.minecraft.class_743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_743.class})
public class KeyboardInputMixin {
   private List<Float> silentRotationStrafe(float strafe, float forward, float yaw) {
      int difference = (int)((class_3532.method_15393(ApeClient.mc.field_1724.method_36454() - yaw - 23.5F - 135.0F) + 180.0F) / 45.0F);
      float calcForward = 0.0F;
      float calcStrafe = 0.0F;
      switch (difference) {
         case 0:
            calcForward = forward;
            calcStrafe = strafe;
            break;
         case 1:
            calcForward += forward;
            calcStrafe -= forward;
            calcForward += strafe;
            calcStrafe += strafe;
            break;
         case 2:
            calcForward = strafe;
            calcStrafe = -forward;
            break;
         case 3:
            calcForward -= forward;
            calcStrafe -= forward;
            calcForward += strafe;
            calcStrafe -= strafe;
            break;
         case 4:
            calcForward = -forward;
            calcStrafe = -strafe;
            break;
         case 5:
            calcForward -= forward;
            calcStrafe += forward;
            calcForward -= strafe;
            calcStrafe -= strafe;
            break;
         case 6:
            calcForward = -strafe;
            calcStrafe = forward;
            break;
         case 7:
            calcForward += forward;
            calcStrafe += forward;
            calcForward -= strafe;
            calcStrafe += strafe;
      }

      if (calcForward > 1.0F || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1.0F || calcForward > -0.9F && calcForward < -0.3F) {
         calcForward *= 0.5F;
      }

      if (calcStrafe > 1.0F || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1.0F || calcStrafe > -0.9F && calcStrafe < -0.3F) {
         calcStrafe *= 0.5F;
      }

      List<Float> returned = Lists.newArrayList();
      returned.add(calcForward);
      returned.add(calcStrafe);
      return returned;
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z",
         ordinal = 5
      )}
   )
   private void tick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
      if (MovementData.event.modified && MovementData.event.movementCorrection) {
         List<Float> rots = this.silentRotationStrafe(
            ApeClient.mc.field_1724.field_3913.field_3907, ApeClient.mc.field_1724.field_3913.field_3905, MovementData.event.yaw
         );
         ApeClient.mc.field_1724.field_3913.field_3905 = MovementData.event.customInputX != 0 ? (float)MovementData.event.customInputX : rots.get(0);
         ApeClient.mc.field_1724.field_3913.field_3907 = MovementData.event.customInputZ != 0 ? (float)MovementData.event.customInputZ : rots.get(1);
      }
   }
}
