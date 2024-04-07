package net.ape.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.RenderHudEvent;
import net.ape.gui.ClickGui;
import net.ape.modules.Module;
import net.minecraft.class_124;
import net.minecraft.class_4587;

public class Health extends Module {
   final DecimalFormat f = new DecimalFormat("0.#");

   public Health() {
      super("Health", ClickGui.render);
   }

   @EventListener
   public void render(RenderHudEvent event) {
      if (ApeClient.mc.field_1724 != null) {
         double hp = (double)((float)Math.round(ApeClient.mc.field_1724.method_6032() + ApeClient.mc.field_1724.method_6067()) / 2.0F);
         double percent = (double)(ApeClient.mc.field_1724.method_6032() / ApeClient.mc.field_1724.method_6063());
         String hpString = this.f.format(hp) + (ApeClient.mc.field_1724.method_6067() != 0.0F ? class_124.field_1065 : class_124.field_1061) + " ❤";
         int y = ApeClient.mc.method_22683().method_4506() / 2 + 19;
         int healthColor = new Color(2, 190, 58).getRGB();
         if (percent <= 0.75) {
            healthColor = new Color(255, 249, 18).getRGB();
         }

         if (percent <= 0.25) {
            healthColor = new Color(255, 20, 20).getRGB();
         }

         int x = ApeClient.mc.method_22683().method_4489() / 2 - ApeClient.mc.field_1772.method_1727(this.f.format(hp)) - 1;
         class_4587 matrices = event.context.method_51448();
         matrices.method_22903();
         matrices.method_22905(2.0F, 2.0F, 2.0F);
         matrices.method_46416((float)(-(x / 2)), (float)(-(y / 2)), 0.0F);
         event.context.method_51433(ApeClient.mc.field_1772, hpString, x, y, healthColor, true);
         matrices.method_22909();
      }
   }
}
