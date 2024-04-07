package net.ape.mixins;

import net.ape.events.CameraUpdateEvent;
import net.ape.handlers.EventHandler;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_312.class})
public class MouseMixin {
   private long updateTime = System.currentTimeMillis();

   @Inject(
      method = {"updateMouse"},
      at = {@At("TAIL")}
   )
   private void updateMouse(CallbackInfo ci) {
      float timePassed = (float)(System.currentTimeMillis() - this.updateTime) / 1000.0F;
      EventHandler.fireEvent(new CameraUpdateEvent(timePassed, 0, 0));
      this.updateTime = System.currentTimeMillis();
   }
}
