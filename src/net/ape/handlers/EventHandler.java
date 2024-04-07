package net.ape.handlers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;
import net.ape.events.CameraUpdateEvent;
import net.ape.events.CollisionEvent;
import net.ape.events.EntityRenderEvent;
import net.ape.events.Event;
import net.ape.events.EventListener;
import net.ape.events.GameTickEvent;
import net.ape.events.JumpEvent;
import net.ape.events.KeyPollingEvent;
import net.ape.events.MoveEvent;
import net.ape.events.PacketEvent;
import net.ape.events.PlayerMoveEvent;
import net.ape.events.RenderHudEvent;
import net.ape.events.VelocityEvent;
import net.ape.events.WorldChangeEvent;
import net.ape.modules.Module;

public class EventHandler {
   public static HashMap<Class, HashMap<Module, Method>> map = new HashMap<>();

   public static void init() {
      map.put(GameTickEvent.class, new HashMap<>());
      map.put(KeyPollingEvent.class, new HashMap<>());
      map.put(MoveEvent.class, new HashMap<>());
      map.put(JumpEvent.class, new HashMap<>());
      map.put(VelocityEvent.class, new HashMap<>());
      map.put(PacketEvent.class, new HashMap<>());
      map.put(EntityRenderEvent.class, new HashMap<>());
      map.put(RenderHudEvent.class, new HashMap<>());
      map.put(PlayerMoveEvent.class, new HashMap<>());
      map.put(CameraUpdateEvent.class, new HashMap<>());
      map.put(CollisionEvent.class, new HashMap<>());
      map.put(WorldChangeEvent.class, new HashMap<>());

      for (Module mod : ModuleHandler.modules) {
         for (Method method : mod.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
               Class[] requiredTypes = method.getParameterTypes();

               for (Class cl : requiredTypes) {
                  if (map.containsKey(cl)) {
                     map.get(cl).put(mod, method);
                  }
               }
            }
         }
      }
   }

   public static boolean fireEvent(Event e) {
      for (Entry<Module, Method> set : map.get(e.getClass()).entrySet()) {
         if (e.isCanceled()) {
            break;
         }

         if (set.getKey().enabled) {
            try {
               set.getValue().invoke(set.getKey(), e);
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }

      return e.isCanceled();
   }
}
