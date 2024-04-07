package net.ape.modules.blatant;

import java.util.Iterator;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.GameTickEvent;
import net.ape.events.PacketEvent;
import net.ape.events.VelocityEvent;
import net.ape.gui.ClickGui;
import net.ape.modules.Module;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_265;
import net.minecraft.class_2828;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2828.class_5911;

public class Disabler extends Module {
   public int elevation = 1;

   public Disabler() {
      super("Disabler", ClickGui.blatant);
   }

   private int canAttack() {
      for (class_1657 ent : ApeClient.mc.field_1687.method_18456()) {
         if (ent != ApeClient.mc.field_1724 && ApeClient.mc.field_1724.method_5739(ent) < 10.0F) {
            return 2;
         }
      }

      return 1;
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @EventListener
   public void velocityEvent(VelocityEvent event) {
      event.cancel();
   }

   @EventListener
   public void packetEvent(PacketEvent e) {
      if (e.packet instanceof class_2828 packet) {
         e.cancel();
         if (packet.method_36172() && packet.method_36171()) {
            ApeClient.mc
               .method_1562()
               .method_48296()
               .method_52906(
                  new class_2830(
                     packet.method_12269(ApeClient.mc.field_1724.method_23317()),
                     packet.method_12268(ApeClient.mc.field_1724.method_23318()),
                     packet.method_12274(ApeClient.mc.field_1724.method_23321()),
                     packet.method_12271(ApeClient.mc.field_1724.method_36454()),
                     packet.method_12270(ApeClient.mc.field_1724.method_36455()),
                     false
                  ),
                  null,
                  false
               );
         } else if (packet.method_36171()) {
            ApeClient.mc
               .method_1562()
               .method_48296()
               .method_52906(
                  new class_2829(
                     packet.method_12269(ApeClient.mc.field_1724.method_23317()),
                     packet.method_12268(ApeClient.mc.field_1724.method_23318()),
                     packet.method_12274(ApeClient.mc.field_1724.method_23321()),
                     false
                  ),
                  null,
                  false
               );
         } else if (packet.method_36172()) {
            ApeClient.mc
               .method_1562()
               .method_48296()
               .method_52906(
                  new class_2831(
                     packet.method_12271(ApeClient.mc.field_1724.method_36454()), packet.method_12270(ApeClient.mc.field_1724.method_36455()), false
                  ),
                  null,
                  false
               );
         } else {
            ApeClient.mc.method_1562().method_48296().method_52906(new class_5911(false), null, true);
         }
      }
   }

   @EventListener
   public void worldTick(GameTickEvent event) {
      if (ApeClient.mc.field_1724 != null) {
         this.elevation = this.canAttack();
         Iterator var2 = ApeClient.mc
            .field_1687
            .method_20812(ApeClient.mc.field_1724, ApeClient.mc.field_1724.method_5829().method_1012(0.0, (double)(-this.elevation), 0.0))
            .iterator();
         if (var2.hasNext()) {
            class_265 vox = (class_265)var2.next();
            ApeClient.mc
               .field_1724
               .method_5814(ApeClient.mc.field_1724.method_23317(), ApeClient.mc.field_1724.method_23318() + 1.0, ApeClient.mc.field_1724.method_23321());
         }

         if (ApeClient.mc.field_1724.method_6059(class_1294.field_5906)) {
            ApeClient.mc.field_1724.method_6016(class_1294.field_5906);
         }
      }
   }

   @Override
   public String extra() {
      return " Hoplite";
   }
}
