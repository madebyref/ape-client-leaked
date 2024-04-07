package net.ape.mixins;

import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.minecraft.class_309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_309.class})
public class KeyboardMixin {
   @Inject(
      method = {"onChar"},
      at = {@At("TAIL")}
   )
   private void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
      if (ApeClient.mc.field_1755 instanceof ClickGui) {
         if (Character.charCount(codePoint) == 1) {
            ApeClient.gui.onChar((char)codePoint);
         } else {
            for (char c : Character.toChars(codePoint)) {
               ApeClient.gui.onChar(c);
            }
         }
      }
   }
}
