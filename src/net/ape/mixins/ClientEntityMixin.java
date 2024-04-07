package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.events.MovementData;
import net.ape.events.PlayerMoveEvent;
import net.ape.handlers.EventHandler;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_304;
import net.minecraft.class_3532;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public abstract class ClientEntityMixin {
   PlayerMoveEvent lastEvent;

   @Shadow
   public abstract float method_5705(float var1);

   @Shadow
   public abstract float method_5695(float var1);

   @Redirect(
      method = {"tickMovement"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
      )
   )
   private boolean isUsingItem(class_746 plr) {
      return !ModuleHandler.noSlow.enabled && plr.method_6115();
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"
      )
   )
   private boolean isPressed(class_304 instance) {
      return instance.method_1435(ApeClient.mc.field_1690.field_1867) && ModuleHandler.sprint.enabled ? true : instance.method_1434();
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void tick(CallbackInfo ci) {
      PlayerMoveEvent event = new PlayerMoveEvent(this.method_5705(0.0F), this.method_5695(0.0F));
      MovementData.event = event;
      EventHandler.fireEvent(event);
      if (event.yaw != this.method_5705(0.0F) || event.pitch != this.method_5695(0.0F)) {
         event.modified = true;
         event.movementCorrection = true;
      }

      if (this.lastEvent != null && this.lastEvent.modified && !event.modified) {
         ApeClient.mc.field_1724.method_36456(this.lastEvent.yaw + class_3532.method_15393(this.method_5705(0.0F) - this.lastEvent.yaw));
         ApeClient.mc.field_1724.field_5982 = ApeClient.mc.field_1724.method_36454();
      }

      this.lastEvent = event;
   }

   @Redirect(
      method = {"sendMovementPackets"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F",
         opcode = 180
      )
   )
   private float getYawHook(class_746 instance) {
      return MovementData.event.modified ? MovementData.event.yaw : instance.method_5705(0.0F);
   }

   @Redirect(
      method = {"sendMovementPackets"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F",
         opcode = 180
      )
   )
   private float getPitchHook(class_746 instance) {
      return MovementData.event.modified ? MovementData.event.pitch : instance.method_5695(0.0F);
   }
}
