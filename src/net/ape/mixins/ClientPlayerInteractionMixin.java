package net.ape.mixins;

import net.ape.events.MovementData;
import net.minecraft.class_1657;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_636.class})
public class ClientPlayerInteractionMixin {
   @Redirect(
      method = {"interactItem"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;getYaw()F",
         opcode = 180
      )
   )
   private float getYawHook(class_1657 instance) {
      return MovementData.event.modified ? MovementData.event.yaw : instance.method_5705(0.0F);
   }

   @Redirect(
      method = {"interactItem"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;getPitch()F",
         opcode = 180
      )
   )
   private float getPitchHook(class_1657 instance) {
      return MovementData.event.modified ? MovementData.event.pitch : instance.method_5695(0.0F);
   }
}
