package net.ape.gui.font.fontrenderer;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import net.minecraft.class_124;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_5611;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TTFFontRenderer {
   private final boolean antiAlias;
   private final Font font;
   private boolean fractionalMetrics = false;
   private TTFFontRenderer.CharacterData[] regularData;
   private TTFFontRenderer.CharacterData[] boldData;
   private TTFFontRenderer.CharacterData[] italicsData;
   private final int[] colorCodes = new int[32];
   private static int RANDOM_OFFSET = 1;

   public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font) {
      this(executorService, textureQueue, font, 256);
   }

   public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font, int characterCount) {
      this(executorService, textureQueue, font, characterCount, true);
   }

   public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font, boolean antiAlias) {
      this(executorService, textureQueue, font, 256, antiAlias);
   }

   public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font, int characterCount, boolean antiAlias) {
      this.font = font;
      this.fractionalMetrics = true;
      this.antiAlias = antiAlias;
      int[] regularTexturesIds = new int[characterCount];
      int[] boldTexturesIds = new int[characterCount];
      int[] italicTexturesIds = new int[characterCount];

      for (int i = 0; i < characterCount; i++) {
         regularTexturesIds[i] = GL11.glGenTextures();
         boldTexturesIds[i] = GL11.glGenTextures();
         italicTexturesIds[i] = GL11.glGenTextures();
      }

      executorService.execute(() -> this.regularData = this.setup(new TTFFontRenderer.CharacterData[characterCount], regularTexturesIds, textureQueue, 0));
      executorService.execute(() -> this.boldData = this.setup(new TTFFontRenderer.CharacterData[characterCount], boldTexturesIds, textureQueue, 1));
      executorService.execute(() -> this.italicsData = this.setup(new TTFFontRenderer.CharacterData[characterCount], italicTexturesIds, textureQueue, 2));
   }

   private TTFFontRenderer.CharacterData[] setup(
      TTFFontRenderer.CharacterData[] characterData, int[] texturesIds, ConcurrentLinkedQueue<TextureData> textureQueue, int type
   ) {
      this.generateColors();
      Font font = this.font.deriveFont(type);
      BufferedImage utilityImage = new BufferedImage(1, 1, 2);
      Graphics2D utilityGraphics = (Graphics2D)utilityImage.getGraphics();
      utilityGraphics.setFont(font);
      FontMetrics fontMetrics = utilityGraphics.getFontMetrics();

      for (int index = 0; index < characterData.length; index++) {
         char character = (char)index;
         Rectangle2D characterBounds = fontMetrics.getStringBounds(character + "", utilityGraphics);
         float width = (float)characterBounds.getWidth() + 8.0F;
         float height = (float)characterBounds.getHeight();
         BufferedImage characterImage = new BufferedImage(class_3532.method_15386(width), class_3532.method_15386(height), 2);
         Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
         graphics.setFont(font);
         graphics.setColor(new Color(255, 255, 255, 0));
         graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
         graphics.setColor(Color.WHITE);
         if (this.antiAlias) {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(
               RenderingHints.KEY_FRACTIONALMETRICS,
               this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF
            );
         }

         graphics.drawString(character + "", 4, fontMetrics.getAscent());
         int textureId = texturesIds[index];
         this.createTexture(textureId, characterImage, textureQueue);
         characterData[index] = new TTFFontRenderer.CharacterData(character, (float)characterImage.getWidth(), (float)characterImage.getHeight(), textureId);
      }

      return characterData;
   }

   private void createTexture(int textureId, BufferedImage image, ConcurrentLinkedQueue<TextureData> textureQueue) {
      int[] pixels = new int[image.getWidth() * image.getHeight()];
      image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
      ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            int pixel = pixels[y * image.getWidth() + x];
            buffer.put((byte)(pixel >> 16 & 0xFF));
            buffer.put((byte)(pixel >> 8 & 0xFF));
            buffer.put((byte)(pixel & 0xFF));
            buffer.put((byte)(pixel >> 24 & 0xFF));
         }
      }

      buffer.flip();
      textureQueue.add(new TextureData(textureId, image.getWidth(), image.getHeight(), buffer));
   }

   public void drawString(class_332 context, String text, float x, float y, int color, boolean shadow) {
      RenderSystem.disableBlend();
      if (shadow) {
         this.renderString(context, class_124.method_539(text), x + 1.0F, y + 1.0F, new Color(0, 0, 0, 180).hashCode(), false, false);
      }

      this.renderString(context, text, x, y, color, false, true);
      RenderSystem.enableBlend();
   }

   public void drawCenteredString(class_332 context, String text, float x, float y, int color) {
      float width = this.getWidth(text);
      this.renderString(context, text, x - width, y, color, false);
   }

   public void drawStringWithShadow(class_332 context, String text, float x, float y, int color) {
      GL11.glTranslated(0.5, 0.5, 0.0);
      this.renderString(context, text, x, y, color, true);
      GL11.glTranslated(-0.5, -0.5, 0.0);
      this.renderString(context, text, x, y, color, false);
   }

   private int renderString(class_332 context, String text, float x, float y, int color, boolean shadow) {
      if (!text.equals("") && text.length() != 0) {
         x = (float)Math.round(x * 10.0F) / 10.0F;
         y = (float)Math.round(y * 10.0F) / 10.0F;
         context.method_51448().method_22903();
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(770, 771);
         x -= 3.0F;
         y -= 3.0F;
         TTFFontRenderer.CharacterData[] characterData = this.regularData;
         int length = text.length();
         double multiplier = 255.0 * (double)(shadow ? 4 : 1);
         Color c = new Color(color);
         context.method_51422(
            (float)((double)c.getRed() / multiplier),
            (float)((double)c.getGreen() / multiplier),
            (float)((double)c.getBlue() / multiplier),
            (float)((double)(color >> 24 & 0xFF) / 255.0)
         );

         try {
            for (int i = 0; i < length; i++) {
               char character = text.charAt(i);
               if (character < characterData.length) {
                  TTFFontRenderer.CharacterData charData = characterData[character];
                  x += charData.width - 8.0F;
               }
            }
         } catch (StringIndexOutOfBoundsException var15) {
            System.out.println("[Rise] Couldn't render text");
            var15.printStackTrace();
         }

         context.method_51448().method_22909();
         RenderSystem.disableBlend();
         RenderSystem.bindTexture(0);
         RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
         return (int)x;
      } else {
         return 0;
      }
   }

   private int renderString(class_332 context, String text, float x, float y, int color, boolean shadow, boolean nickga) {
      if (!text.equals("") && text.length() != 0) {
         x = (float)Math.round(x * 10.0F) / 10.0F;
         y = (float)Math.round(y * 10.0F) / 10.0F;
         context.method_51448().method_22903();
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(770, 771);
         x -= 3.0F;
         y -= 3.0F;
         TTFFontRenderer.CharacterData[] characterData = this.regularData;
         boolean underlined = false;
         boolean strikethrough = false;
         boolean obfuscated = false;
         int length = text.length();
         double multiplier = 255.0 * (double)(shadow ? 4 : 1);
         Color c = new Color(color);
         context.method_51422(
            (float)((double)c.getRed() / multiplier),
            (float)((double)c.getGreen() / multiplier),
            (float)((double)c.getBlue() / multiplier),
            (float)(color >> 24 & 0xFF) / 255.0F
         );

         for (int i = 0; i < length; i++) {
            char character = text.charAt(i);
            char previous = i > 0 ? text.charAt(i - 1) : 46;
            if (previous != 167) {
               if (character == 167) {
                  try {
                     int index = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                     if (index < 16) {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.regularData;
                        if (index < 0) {
                           index = 15;
                        }

                        if (shadow) {
                           index += 16;
                        }

                        int textColor = this.colorCodes[index];
                        context.method_51422(
                           (float)(textColor >> 16) / 255.0F,
                           (float)(textColor >> 8 & 0xFF) / 255.0F,
                           (float)(textColor & 0xFF) / 255.0F,
                           (float)(color >> 24 & 0xFF) / 255.0F
                        );
                     } else if (index <= 20) {
                        switch (index) {
                           case 16:
                              obfuscated = true;
                              break;
                           case 17:
                              characterData = this.boldData;
                              break;
                           case 18:
                              strikethrough = true;
                              break;
                           case 19:
                              underlined = true;
                              break;
                           case 20:
                              characterData = this.italicsData;
                        }
                     } else {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.regularData;
                        context.method_51422(shadow ? 0.25F : 1.0F, shadow ? 0.25F : 1.0F, shadow ? 0.25F : 1.0F, (float)(color >> 24 & 0xFF) / 255.0F);
                     }
                  } catch (StringIndexOutOfBoundsException var21) {
                  }
               } else if (character <= 255) {
                  if (obfuscated) {
                     character += (char)RANDOM_OFFSET;
                  }

                  this.drawChar(context, character, characterData, x, y);
                  TTFFontRenderer.CharacterData charData = characterData[character];
                  if (strikethrough) {
                     this.drawLine(new class_5611(0.0F, charData.height / 2.0F), new class_5611(charData.width, charData.height / 2.0F), 3.0F);
                  }

                  if (underlined) {
                     this.drawLine(new class_5611(0.0F, charData.height - 15.0F), new class_5611(charData.width, charData.height - 15.0F), 3.0F);
                  }

                  x += charData.width - 8.0F;
               }
            }
         }

         context.method_51448().method_22909();
         RenderSystem.disableBlend();
         RenderSystem.bindTexture(0);
         RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
         return (int)x;
      } else {
         return 0;
      }
   }

   public float getWidth(String text) {
      float width = 0.0F;
      TTFFontRenderer.CharacterData[] characterData = this.regularData;
      text = class_124.method_539(text);

      try {
         int length = text.length();

         for (int i = 0; i < length; i++) {
            char character = text.charAt(i);
            TTFFontRenderer.CharacterData charData = characterData[character];
            width += charData.width - 8.0F;
         }
      } catch (ArrayIndexOutOfBoundsException var8) {
         return this.getWidth("A");
      }

      return width + 2.0F;
   }

   public float getWidthProtect(String text) {
      float width = 0.0F;
      TTFFontRenderer.CharacterData[] characterData = this.regularData;
      int length = text.length();

      for (int i = 0; i < length; i++) {
         char character = text.charAt(i);
         TTFFontRenderer.CharacterData charData = characterData[character];
         width += (charData.width - 8.0F) / 2.0F;
      }

      return width + 2.0F;
   }

   public float getHeight(String text) {
      float height = 0.0F;
      TTFFontRenderer.CharacterData[] characterData = this.regularData;
      int length = text.length();

      for (int i = 0; i < length; i++) {
         char character = text.charAt(i);
         TTFFontRenderer.CharacterData charData = characterData[character];
         height = Math.max(height, charData.height);
      }

      return height / 2.0F - 2.0F;
   }

   public float getHeight() {
      return this.getHeight("I");
   }

   private void drawChar(class_332 context, char character, TTFFontRenderer.CharacterData[] characterData, float x, float y) {
      if (character < characterData.length) {
         TTFFontRenderer.CharacterData charData = characterData[character];
         charData.bind();
         RenderSystem.setShader(class_757::method_34542);
         Matrix4f matrix4f = context.method_51448().method_23760().method_23761();
         class_287 bufferBuilder = class_289.method_1348().method_1349();
         bufferBuilder.method_1328(class_5596.field_27382, class_290.field_1585);
         bufferBuilder.method_22918(matrix4f, x, y, 0.0F).method_22913(0.0F, 0.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, x, y + charData.height, 0.0F).method_22913(0.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, x + charData.width, y + charData.height, 0.0F).method_22913(1.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, x + charData.width, y, 0.0F).method_22913(1.0F, 0.0F).method_1344();
         class_286.method_43433(bufferBuilder.method_1326());
      }
   }

   private void drawLine(class_5611 start, class_5611 end) {
      GL11.glDisable(3553);
      GL11.glLineWidth(3.0F);
      GL11.glBegin(1);
      GL11.glVertex2f(start.method_32118(), start.method_32119());
      GL11.glVertex2f(end.method_32118(), end.method_32119());
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   private void drawLine(class_5611 start, class_5611 end, float width) {
      GL11.glDisable(3553);
      GL11.glLineWidth(width);
      GL11.glBegin(1);
      GL11.glVertex2f(start.method_32118(), start.method_32119());
      GL11.glVertex2f(end.method_32118(), end.method_32119());
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   private void generateColors() {
      for (int i = 0; i < 32; i++) {
         int thingy = (i >> 3 & 1) * 85;
         int red = (i >> 2 & 1) * 170 + thingy;
         int green = (i >> 1 & 1) * 170 + thingy;
         int blue = (i & 1) * 170 + thingy;
         if (i == 6) {
            red += 85;
         }

         if (i >= 16) {
            red /= 4;
            green /= 4;
            blue /= 4;
         }

         this.colorCodes[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
      }
   }

   public String trimStringToWidth(String text, int width) {
      return this.trimStringToWidth(text, width, false);
   }

   public String trimStringToWidth(String text, int width, boolean reverse) {
      StringBuilder stringbuilder = new StringBuilder();
      float f = 0.0F;
      int i = reverse ? text.length() - 1 : 0;
      int j = reverse ? -1 : 1;
      boolean flag = false;
      boolean flag1 = false;

      for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j) {
         char c0 = text.charAt(k);
         float f1 = this.getWidth(String.valueOf(c0));
         if (flag) {
            flag = false;
            if (c0 == 'l' || c0 == 'L') {
               flag1 = true;
            } else if (c0 == 'r' || c0 == 'R') {
               flag1 = false;
            }
         } else if (f1 < 0.0F) {
            flag = true;
         } else {
            f += f1;
            if (flag1) {
               f++;
            }
         }

         if (f > (float)width) {
            break;
         }

         if (reverse) {
            stringbuilder.insert(0, c0);
         } else {
            stringbuilder.append(c0);
         }
      }

      return stringbuilder.toString();
   }

   static class CharacterData {
      public char character;
      public float width;
      public float height;
      private final int textureId;

      public CharacterData(char character, float width, float height, int textureId) {
         this.character = character;
         this.width = width;
         this.height = height;
         this.textureId = textureId;
      }

      public void bind() {
         RenderSystem.setShaderTexture(0, this.textureId);
      }
   }
}
