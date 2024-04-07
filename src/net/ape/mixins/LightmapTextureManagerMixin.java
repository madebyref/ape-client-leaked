package net.ape.mixins;

import net.ape.handlers.ModuleHandler;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({class_765.class})
public class LightmapTextureManagerMixin {
   @ModifyVariable(
      method = {"update"},
      at = @At("STORE"),
      ordinal = 10
   )
   private float gamma(float gamma) {
      return ModuleHandler.fullbright.enabled ? 10.0F : gamma;
   }
}
