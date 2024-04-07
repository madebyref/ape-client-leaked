package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.ape.handlers.ModuleHandler;
import net.ape.handlers.NotificationHandler;
import net.ape.modules.Module;
import net.minecraft.class_124;
import net.minecraft.class_304;
import net.minecraft.class_3675.class_306;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_304.class})
public class KeyBindingMixin {
   @Inject(
      method = {"onKeyPressed"},
      at = {@At("HEAD")}
   )
   private static void pressed(class_306 key, CallbackInfo ci) {
      if (key.method_1444() == 344) {
         ApeClient.mc.method_1507(ApeClient.gui);
      } else {
         for (Module mod : ModuleHandler.modules) {
            if (key.method_1444() == mod.key) {
               mod.toggle();
               if (ClickGui.pane5.togglenotif.enabled) {
                  NotificationHandler.addNotification(
                     "Module Toggled",
                     mod.name
                        + class_124.field_1068
                        + " has been "
                        + (mod.enabled ? class_124.field_1060 + "Enabled" : class_124.field_1061 + "Disabled")
                        + class_124.field_1068
                        + "!",
                     0.75
                  );
               }
            }
         }
      }
   }
}
