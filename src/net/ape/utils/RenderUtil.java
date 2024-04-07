package net.ape.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_5253.class_5254;
import org.joml.Matrix4f;

public class RenderUtil {
   static class_2960 circle = new class_2960("ape/circle.png");
   public static Color white = new Color(255, 255, 255);

   public static void rect(class_332 context, int x, int y, int sizeX, int sizeY, Color color) {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      context.method_25294(x, y, x + sizeX, y + sizeY, color.getRGB());
      RenderSystem.enableBlend();
   }

   public static void rect(class_332 context, float x, float y, float sizeX, float sizeY, Color color) {
      rect(context, (int)x, (int)y, (int)sizeX, (int)sizeY, color);
   }

   public static void roundedRect(class_332 context, double x, double y, double width, double height, double edgeRadius, Color color) {
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(
         (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
      );
      context.method_25290(circle, (int)x, (int)y, 0.0F, 0.0F, 4, 4, 9, 9);
      context.method_25290(circle, (int)(x + width - 4.0), (int)y, 5.0F, 0.0F, 4, 4, 9, 9);
      context.method_25290(circle, (int)x, (int)(y + height - 4.0), 0.0F, 5.0F, 4, 4, 9, 9);
      context.method_25290(circle, (int)(x + width - 4.0), (int)(y + height - 4.0), 5.0F, 5.0F, 4, 4, 9, 9);
      rect(context, (int)x + 4, (int)y, (int)width - 8, (int)height, color);
      rect(context, (int)x, (int)y + 4, 4, (int)height - 8, color);
      rect(context, (int)(x + width - 4.0), (int)y + 4, 4, (int)height - 8, color);
   }

   public static void smoothRoundedRect(class_332 context, double x, double y, double width, double height, double edgeRadius, Color color) {
      roundedRect(context, x, y, width, height, edgeRadius, color);
   }

   public static void image(class_332 context, class_2960 loc, int posX, int posY, int width, int height, Color color) {
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(
         (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
      );
      context.method_25290(loc, posX, posY, 0.0F, 0.0F, width, height, width, height);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void image(
      class_332 context, class_2960 loc, int x, int y, int u, int v, int uWidth, int vHeight, int width, int height, int tileWidth, int tileHeight
   ) {
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      context.method_25293(loc, x, y, width, height, (float)u, (float)v, uWidth, vHeight, tileWidth, tileHeight);
   }

   public static void image(class_332 context, class_2960 loc, int posX, int posY, int width, int height) {
      image(context, loc, posX, posY, width, height, white);
   }

   public static void gradientSideways(class_332 context, int x, int y, int width, int height, Color sat, Color newColor) {
      fillGradient(context, x, y, x + width, y + height, sat.getRGB(), newColor.getRGB());
   }

   private static void fillGradient(class_332 context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
      fillGradient(context, startX, startY, endX, endY, 0, colorStart, colorEnd);
   }

   private static void fillGradient(class_332 context, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
      fillGradient(context, class_1921.method_51784(), startX, startY, endX, endY, colorStart, colorEnd, z);
   }

   private static void fillGradient(class_332 context, class_1921 layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
      class_4588 vertexConsumer = context.method_51450().getBuffer(layer);
      fillGradient(context, vertexConsumer, startX, startY, endX, endY, z, colorStart, colorEnd);
      context.method_51452();
   }

   private static void fillGradient(
      class_332 context, class_4588 vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd
   ) {
      float f = (float)class_5254.method_27762(colorStart) / 255.0F;
      float g = (float)class_5254.method_27765(colorStart) / 255.0F;
      float h = (float)class_5254.method_27766(colorStart) / 255.0F;
      float i = (float)class_5254.method_27767(colorStart) / 255.0F;
      float j = (float)class_5254.method_27762(colorEnd) / 255.0F;
      float k = (float)class_5254.method_27765(colorEnd) / 255.0F;
      float l = (float)class_5254.method_27766(colorEnd) / 255.0F;
      float m = (float)class_5254.method_27767(colorEnd) / 255.0F;
      Matrix4f matrix4f = context.method_51448().method_23760().method_23761();
      vertexConsumer.method_22918(matrix4f, (float)startX, (float)startY, (float)z).method_22915(g, h, i, f).method_1344();
      vertexConsumer.method_22918(matrix4f, (float)startX, (float)endY, (float)z).method_22915(g, h, i, f).method_1344();
      vertexConsumer.method_22918(matrix4f, (float)endX, (float)endY, (float)z).method_22915(k, l, m, j).method_1344();
      vertexConsumer.method_22918(matrix4f, (float)endX, (float)startY, (float)z).method_22915(k, l, m, j).method_1344();
   }
}
