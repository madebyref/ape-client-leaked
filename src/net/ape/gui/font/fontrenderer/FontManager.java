package net.ape.gui.font.fontrenderer;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Font;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.lwjgl.opengl.GL11;

public final class FontManager {
   private final HashMap<String, TTFFontRenderer> fonts = new HashMap<>();
   private final TTFFontRenderer defaultFont;

   public TTFFontRenderer getFont(String key) {
      return this.fonts.getOrDefault(key, this.defaultFont);
   }

   public FontManager() {
      ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
      ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();
      this.defaultFont = new TTFFontRenderer(executorService, textureQueue, new Font("Calibri", 0, 18));

      for (int i : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 32, 128}) {
         this.fonts.put("Calibri " + i, new TTFFontRenderer(executorService, textureQueue, new Font("Calibri", 0, i)));
         this.fonts.put("Calibri Bold" + i, new TTFFontRenderer(executorService, textureQueue, new Font("Calibri", 1, i)));
         this.fonts.put("Arial " + i, new TTFFontRenderer(executorService, textureQueue, new Font("Arial", 0, i)));
         this.fonts.put("Arial Bold" + i, new TTFFontRenderer(executorService, textureQueue, new Font("Arial", 1, i)));
      }

      executorService.shutdown();

      while (!executorService.isTerminated()) {
         try {
            Thread.sleep(10L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }

         while (!textureQueue.isEmpty()) {
            TextureData textureData = textureQueue.poll();
            RenderSystem.bindTexture(textureData.getTextureId());
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexImage2D(3553, 0, 6408, textureData.getWidth(), textureData.getHeight(), 0, 6408, 5121, textureData.getBuffer());
         }
      }
   }
}
