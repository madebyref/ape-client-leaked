package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.events.MovementData;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1657.class})
public abstract class PlayerEntityMixin {
   @Shadow
   public abstract class_1661 method_31548();

   @ModifyArg(
      method = {"attack"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
      ),
      index = 0
   )
   public class_243 setVelocity(class_243 velocity) {
      return ModuleHandler.keepSprint.enabled && ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_18798() : velocity;
   }

   @ModifyArg(
      method = {"attack"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"
      ),
      index = 0
   )
   public boolean setSprinting(boolean sprinting) {
      ModuleHandler.wTap.sprintHit = true;
      return ModuleHandler.keepSprint.enabled && ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_5624() : sprinting;
   }

   @Inject(
      method = {"tickNewAi"},
      at = {@At("TAIL")}
   )
   public void heh(CallbackInfo ci) {
      if (MovementData.event.modified && this.method_31548().equals(ApeClient.mc.field_1724.method_31548())) {
         ApeClient.mc.field_1724.method_5847(MovementData.event.yaw);
         ApeClient.mc.field_1724.method_5636(class_3532.method_15393(MovementData.event.yaw));
      }
   }

   @Redirect(
      method = {"getBlockBreakingSpeed"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z"
      )
   )
   private boolean onground(class_1657 instance) {
      return ModuleHandler.disabler.enabled ? false : instance.method_24828();
   }
}
