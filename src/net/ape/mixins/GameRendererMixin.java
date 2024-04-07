package net.ape.mixins;

import java.util.function.Predicate;
import net.ape.ApeClient;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1675;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3966;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_757.class})
public class GameRendererMixin {
   @Redirect(
      method = {"updateTargetedEntity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"
      )
   )
   private class_3966 call(class_1297 entity, class_243 min, class_243 max, class_238 box, Predicate<class_1297> predicate, double d) {
      if (ModuleHandler.cobwebBypass.enabled && entity == ApeClient.mc.field_1724) {
         class_1799 hand = ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_5998(class_1268.field_5808) : new class_1799(class_1802.field_8162);
         if (hand.method_7909().equals(class_1802.field_8786)) {
            return null;
         }
      }

      return class_1675.method_18075(entity, min, max, box, predicate, d);
   }
}
