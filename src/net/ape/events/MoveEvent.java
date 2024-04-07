package net.ape.events;

import net.minecraft.class_243;

public class MoveEvent extends Event {
   public class_243 motion;

   public MoveEvent(class_243 motion) {
      this.motion = motion;
   }
}
