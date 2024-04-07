package net.ape.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ape.ApeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderSystem.class})
public class RenderSystemMixin {
   @Inject(
      method = {"initRenderer"},
      at = {@At("TAIL")}
   )
   private static void initRenderer(int debugVerbosity, boolean debugSync, CallbackInfo ci) {
      ApeClient.init();
   }
}
