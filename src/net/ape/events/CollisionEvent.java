package net.ape.events;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_265;

public class CollisionEvent extends Event {
   public List<class_265> list;

   public CollisionEvent(List<class_265> list) {
      this.list = new ArrayList<>(list);
   }
}
