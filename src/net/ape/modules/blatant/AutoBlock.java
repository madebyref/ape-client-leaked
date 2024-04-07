package net.ape.modules.blatant;

import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.KeyPollingEvent;
import net.ape.gui.ClickGui;
import net.ape.mixins.MinecraftClientAccessor;
import net.ape.modules.Module;

public class AutoBlock extends Module {
   public AutoBlock() {
      super("AutoBlock", ClickGui.blatant);
   }

   @EventListener
   public void actionTick(KeyPollingEvent event) {
      if (ApeClient.mc.field_1724.method_6115()) {
         while (ApeClient.mc.field_1690.field_1886.method_1436()) {
            ((MinecraftClientAccessor)ApeClient.mc).invokeDoAttack();
         }
      }
   }
}
