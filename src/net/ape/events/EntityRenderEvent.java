package net.ape.events;

import net.minecraft.class_1297;
import net.minecraft.class_4587;
import net.minecraft.class_4597;

public class EntityRenderEvent extends Event {
   public class_1297 entity;
   public float yaw;
   public float tickDelta;
   public class_4587 matrices;
   public class_4597 vertexConsumerProvider;
   public int light;

   public EntityRenderEvent(class_1297 entity, float yaw, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, int light) {
      this.entity = entity;
      this.yaw = yaw;
      this.tickDelta = tickDelta;
      this.matrices = matrices;
      this.vertexConsumerProvider = vertexConsumers;
      this.light = light;
   }
}
