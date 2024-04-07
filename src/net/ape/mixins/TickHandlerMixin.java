package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_317;
import net.minecraft.class_418;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_317.class})
public class TickHandlerMixin {
   @Shadow
   public float field_1969;

   @Inject(
      method = {"beginRenderTick"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J",
         opcode = 181
      )}
   )
   private void onBeingRenderTick(long a, CallbackInfoReturnable<Integer> info) {
      if (ModuleHandler.timer.enabled && !(ApeClient.mc.field_1755 instanceof class_418) && ApeClient.mc.field_1724 != null) {
         this.field_1969 = this.field_1969 * ((float)ModuleHandler.timer.timer.value / 10.0F);
      }
   }
}
