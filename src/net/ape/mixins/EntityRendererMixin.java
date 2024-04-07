package net.ape.mixins;

import net.ape.events.EntityRenderEvent;
import net.ape.handlers.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_897.class})
public class EntityRendererMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(class_1297 entity, float yaw, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      if (EventHandler.fireEvent(new EntityRenderEvent(entity, yaw, tickDelta, matrices, vertexConsumers, light))) {
         ci.cancel();
      }
   }
}
