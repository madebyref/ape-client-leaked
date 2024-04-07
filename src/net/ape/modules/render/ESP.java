package net.ape.modules.render;

import net.ape.events.EntityRenderEvent;
import net.ape.events.EventListener;
import net.ape.gui.ClickGui;
import net.ape.modules.Module;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1921;
import net.minecraft.class_238;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_761;

public class ESP extends Module {
   public ESP() {
      super("ESP", ClickGui.render);
   }

   @EventListener
   public void renderEntity(EntityRenderEvent event) {
      if (event.entity instanceof class_1657) {
         this.render(event.entity, event.matrices, event.vertexConsumerProvider, event.light);
      }
   }

   protected void render(class_1297 entity, class_4587 matrices, class_4597 vertexConsumers, int light) {
      float j = 0.01F;
      class_238 box = entity.method_5829();
      class_761.method_22980(matrices, vertexConsumers.getBuffer(class_1921.method_23594()), -0.5, 0.0, -0.5, 0.5, 2.0, 0.5, 1.0F, 1.0F, 0.0F, 1.0F);
   }
}
