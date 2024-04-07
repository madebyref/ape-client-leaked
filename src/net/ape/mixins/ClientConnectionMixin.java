package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.events.PacketEvent;
import net.ape.events.VelocityEvent;
import net.ape.events.WorldChangeEvent;
import net.ape.handlers.EventHandler;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_2678;
import net.minecraft.class_2743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2535.class})
public abstract class ClientConnectionMixin {
   @Inject(
      method = {"handlePacket"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static <T extends class_2547> void onHandlePacket(class_2596<T> packet, class_2547 listener, CallbackInfo info) {
      if (EventHandler.fireEvent(new PacketEvent(packet, PacketEvent.PacketType.RECEIVE, listener))) {
         info.cancel();
      }

      if (packet instanceof class_2743 veloPacket) {
         if (ApeClient.mc.field_1724 == null || veloPacket.method_11818() != ApeClient.mc.field_1724.method_5628()) {
            return;
         }

         if (EventHandler.fireEvent(
            new VelocityEvent(
               (double)veloPacket.method_11815() / 8000.0,
               (double)veloPacket.method_11816() / 8000.0,
               (double)veloPacket.method_11819() / 8000.0,
               VelocityEvent.VelocityType.NORMAL
            )
         )) {
            info.cancel();
         }
      }

      if (packet instanceof class_2678) {
         EventHandler.fireEvent(new WorldChangeEvent());
      }
   }

   @Inject(
      method = {"send"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void send(class_2596<?> packet, CallbackInfo info) {
      if (EventHandler.fireEvent(new PacketEvent(packet, PacketEvent.PacketType.SENT, null))) {
         info.cancel();
      }
   }
}
