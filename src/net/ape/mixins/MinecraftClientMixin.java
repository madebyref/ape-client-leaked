package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.events.GameTickEvent;
import net.ape.events.KeyPollingEvent;
import net.ape.events.MovementData;
import net.ape.handlers.EventHandler;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public abstract class MinecraftClientMixin {
   @Shadow
   public abstract class_1041 method_22683();

   @Shadow
   public abstract void method_1507(@Nullable class_437 var1);

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void tick(CallbackInfo ci) {
      EventHandler.fireEvent(new GameTickEvent());
   }

   @Inject(
      method = {"handleInputEvents"},
      at = {@At("HEAD")}
   )
   private void injectMethod(CallbackInfo info) {
      EventHandler.fireEvent(new KeyPollingEvent());
   }

   @Inject(
      method = {"handleInputEvents"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
      )},
      cancellable = true
   )
   private void cancelCheck(CallbackInfo ci) {
      if (MovementData.event.modified) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"stop"},
      at = {@At("HEAD")}
   )
   private void stop(CallbackInfo info) {
      ApeClient.save(ApeClient.currentConfig);
   }

   @Inject(
      method = {"disconnect"},
      at = {@At("HEAD")}
   )
   private void onDisconnect(CallbackInfo info) {
      ApeClient.save(ApeClient.currentConfig);
   }
}
