package net.ape.mixins;

import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1747.class})
public class BlockItemMixin {
   @Inject(
      method = {"canPlace"},
      at = {@At(
         value = "INVOKE_ASSIGN",
         target = "Lnet/minecraft/world/World;canPlace(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Z"
      )},
      cancellable = true
   )
   private void place(class_1750 context, class_2680 state, CallbackInfoReturnable<Boolean> cir) {
   }
}
