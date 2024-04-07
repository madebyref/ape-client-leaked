package net.ape.mixins;

import java.util.List;
import java.util.UUID;
import net.ape.ApeClient;
import net.ape.events.CollisionEvent;
import net.ape.events.MovementData;
import net.ape.handlers.EventHandler;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_265;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_1297.class})
public abstract class EntityMixin {
   @Shadow
   public float field_6004;

   @Shadow
   public abstract int method_5628();

   @Shadow
   public abstract UUID method_5667();

   @ModifyArgs(
      method = {"updateVelocity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"
      )
   )
   public void movementInputToVelocity(Args args) {
      if (ApeClient.mc.field_1724 != null && this.method_5628() == ApeClient.mc.field_1724.method_5628()) {
         if (MovementData.event.modified && MovementData.event.movementCorrection) {
            args.set(2, MovementData.event.yaw);
         }
      }
   }

   @Redirect(
      method = {"adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"
      )
   )
   private class_238 getBoundingBox(class_1297 instance) {
      if (ApeClient.mc.field_1724 == null || this.method_5628() != ApeClient.mc.field_1724.method_5628()) {
         return instance.method_5829();
      } else {
         return ModuleHandler.disabler.enabled
            ? instance.method_5829().method_1012(0.0, (double)(-ModuleHandler.disabler.elevation), 0.0)
            : instance.method_5829();
      }
   }

   @ModifyArgs(
      method = {"adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
         ordinal = 0
      )
   )
   private void adjustCollisions(Args args) {
      if (args.get(0).equals(ApeClient.mc.field_1724)) {
         List<class_265> list = (List<class_265>)args.get(4);
         CollisionEvent event = new CollisionEvent(list);
         EventHandler.fireEvent(event);
         args.set(4, event.list);
      }
   }
}
