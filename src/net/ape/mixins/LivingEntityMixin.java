package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.events.JumpEvent;
import net.ape.events.MoveEvent;
import net.ape.events.MovementData;
import net.ape.handlers.EventHandler;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_4095;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1309.class})
public abstract class LivingEntityMixin {
   @Shadow
   public abstract class_4095<?> method_18868();

   @Inject(
      method = {"jump"},
      at = {@At("TAIL")}
   )
   private void jump(CallbackInfo ci) {
      if (ApeClient.mc.field_1724 != null && this.method_18868().equals(ApeClient.mc.field_1724.method_18868())) {
         EventHandler.fireEvent(new JumpEvent());
      }
   }

   @Redirect(
      method = {"jump"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getYaw()F",
         opcode = 180
      )
   )
   private float getYawHook(class_1309 instance) {
      return MovementData.event.modified && instance instanceof class_746 ? MovementData.event.yaw : instance.method_5705(0.0F);
   }

   @ModifyVariable(
      method = {"travel"},
      at = @At("STORE"),
      ordinal = 1
   )
   private double injected(double x) {
      if (ApeClient.mc.field_1724 != null
         && this.method_18868().equals(ApeClient.mc.field_1724.method_18868())
         && x == ApeClient.mc.field_1724.method_18798().field_1351 - 0.08) {
         double motionY = ApeClient.mc.field_1724.method_18798().field_1351;
         if (ModuleHandler.gravity.enabled && (!ModuleHandler.gravity.fall.enabled || motionY <= 0.0)) {
            String var5 = ModuleHandler.gravity.mode.value;
            switch (var5) {
               case "Verus":
                  return Math.max(x, -0.08);
               case "Vulcan":
                  if (motionY < -0.099) {
                     x = -0.099;
                     break;
                  }
               default:
                  x = motionY - (double)ModuleHandler.gravity.gravity.value / 1000.0;
            }
         }
      }

      return x;
   }

   @Inject(
      method = {"applyClimbingSpeed"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void move(class_243 motion, CallbackInfoReturnable<class_243> cir) {
      if (ApeClient.mc.field_1724 != null && this.method_18868().equals(ApeClient.mc.field_1724.method_18868())) {
         if (MovementData.event.modified) {
            ApeClient.mc.field_1724.field_6004 = MovementData.event.pitch;
         }

         MoveEvent event = new MoveEvent(ApeClient.mc.field_1724.method_18798());
         EventHandler.fireEvent(event);
         if (!ApeClient.mc.field_1724.method_6101()) {
            cir.setReturnValue(event.motion);
         }
      }
   }
}
