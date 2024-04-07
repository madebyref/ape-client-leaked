package net.ape.mixins;

import net.ape.events.MovementData;
import net.ape.handlers.ModuleHandler;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_746;
import net.minecraft.class_922;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_922.class})
public class LivingEntityRendererMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   public void render(class_1309 livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo e) {
      if (ModuleHandler.chams.enabled && livingEntity instanceof class_1657 && !(livingEntity instanceof class_746)) {
         GL11.glEnable(32823);
         GL11.glPolygonOffset(1.0F, -1100000.0F);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void renderend(class_1309 livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo e) {
      if (ModuleHandler.chams.enabled && livingEntity instanceof class_1657 && !(livingEntity instanceof class_746)) {
         GL11.glPolygonOffset(1.0F, 1100000.0F);
         GL11.glDisable(32823);
      }
   }

   @Redirect(
      method = {"render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"
      )
   )
   private float stuff(class_1309 instance) {
      return instance instanceof class_746 && MovementData.event.modified ? MovementData.event.pitch : instance.method_36455();
   }
}
