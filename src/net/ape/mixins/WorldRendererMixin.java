package net.ape.mixins;

import net.ape.ApeClient;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_765;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_761.class})
public class WorldRendererMixin {
   private static int[] getScreenCoords(double x, double y, double z, Matrix4f vec) {
      Vector3f screenCoords = new Vector3f();
      int[] viewport = new int[4];
      GL11.glGetIntegerv(2978, viewport);
      vec.project((float)x, (float)y, (float)z, viewport, screenCoords);
      return new int[]{(int)screenCoords.x, (int)(screenCoords.y - (float)ApeClient.mc.method_22683().method_4506())};
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void render(
      class_4587 matrices,
      float tickDelta,
      long limitTime,
      boolean renderBlockOutline,
      class_4184 camera,
      class_757 gameRenderer,
      class_765 lightmapTextureManager,
      Matrix4f projectionMatrix,
      CallbackInfo ci
   ) {
   }
}
