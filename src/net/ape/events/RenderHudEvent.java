package net.ape.events;

import net.minecraft.class_332;

public class RenderHudEvent extends Event {
   public class_332 context;

   public RenderHudEvent(class_332 context) {
      this.context = context;
   }
}
