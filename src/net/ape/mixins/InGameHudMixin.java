package net.ape.mixins;

import net.ape.ApeClient;
import net.minecraft.class_329;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public class InGameHudMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   public void render(class_332 drawContext, float tickDelta, CallbackInfo callbackInfo) {
      ApeClient.render(drawContext);
   }
}
