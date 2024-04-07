package net.ape.modules.blatant;

import java.util.concurrent.ConcurrentLinkedQueue;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.GameTickEvent;
import net.ape.events.PacketEvent;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.modules.Module;
import net.minecraft.class_2596;
import net.minecraft.class_2879;

public class Blink extends Module {
   Toggle funny = new Toggle("Pulse Abuse", this, null);
   Slider funnyTicks = new Slider("Ticks", this, 1, 40, 6, 1.0);
   public ConcurrentLinkedQueue<class_2596> packets = new ConcurrentLinkedQueue<>();

   public Blink() {
      super("Blink", ClickGui.blatant);
   }

   private void send() {
      for (class_2596 packet : this.packets) {
         ApeClient.mc.method_1562().method_48296().method_52906(packet, null, true);
      }

      this.packets.clear();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.send();
   }

   @EventListener
   public void packetEvent(PacketEvent event) {
      if (event.packetType == PacketEvent.PacketType.SENT && ApeClient.mc.field_1687 != null) {
         this.packets.add(event.packet);
         event.cancel();
         if (this.funny.enabled && event.packet instanceof class_2879) {
            this.send();
         }
      }
   }

   @EventListener
   public void tick(GameTickEvent event) {
      if (ApeClient.mc.field_1724 != null && this.funny.enabled && ApeClient.mc.field_1724.field_6012 % this.funnyTicks.value == 0) {
         this.send();
      }
   }
}
