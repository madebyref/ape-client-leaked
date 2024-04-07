package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.ClickGui;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.Tween;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class OverlayBar extends GuiObject {
   class_2960 icon = new class_2960("ape/overlaysicon.png");
   class_2960 tab = new class_2960("ape/overlaystab.png");
   class_2960 close = new class_2960("ape/close.png");
   Tween transparencyTween = new Tween(0.16, 0.0, 0.0);
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   public boolean expanded;

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         boolean hover = mouseX - this.x > 191 && mouseX - this.x < 215 && mouseY - this.y > 8 && mouseY - this.y < 32;
         Color hoverColor = hover ? ColorPalette.text : ColorPalette.mainl37;
         RenderUtil.rect(context, x, y, 220, 40, ColorPalette.main);
         if (hover) {
            RenderUtil.roundedRect(context, (double)(x + 191), (double)(y + 8), 24.0, 24.0, 22.0, new Color(255, 255, 255, 20));
         }

         RenderUtil.image(context, this.icon, x + 191, y + 8, 24, 24, hoverColor);
         int yLevel = 41;

         for (GuiObject object : ClickGui.main.objects) {
            if (object.visible) {
               yLevel += object.getSize();
            }
         }

         RenderUtil.roundedRect(context, (double)x, (double)ClickGui.main.y, 220.0, (double)yLevel, 5.0, new Color(0, 0, 0, this.transparencyTween.getValue()));
         if (this.expanded) {
            int var15 = 40;

            for (GuiObject objectx : ApeClient.gui.objects) {
               if (objectx instanceof OverlayWindow) {
                  var15 += 40;
               }
            }

            int baseY = y - (var15 - 80);
            RenderUtil.smoothRoundedRect(context, (double)x, (double)(baseY - 40), 220.0, (double)var15, 10.0, ColorPalette.main);
            RenderUtil.rect(context, x, baseY + 1, 220, 1, ColorPalette.mainl04);
            RenderUtil.image(context, this.tab, x + 10, baseY - 25, 14, 12, ColorPalette.text);
            RenderUtil.image(
               context,
               this.close,
               x + 185,
               baseY - 31,
               24,
               24,
               new Color(ColorPalette.textl2.getRed(), ColorPalette.textl2.getGreen(), ColorPalette.textl2.getBlue(), 128)
            );
            this.render.drawString(context, "Overlays", (float)(x + 35), (float)(baseY - 25), ColorPalette.text.getRGB(), false);
            var15 = 1;

            for (GuiObject objectxx : ApeClient.gui.objects) {
               if (objectxx instanceof OverlayWindow) {
                  boolean hover2 = mouseX - this.x > 0 && mouseX - this.x < 220 && mouseY - (baseY + var15) > 0 && mouseY - (baseY + var15) < 40;
                  OverlayWindow window = (OverlayWindow)objectxx;
                  RenderUtil.image(
                     context,
                     window.toggleIcon,
                     x + window.toggleIconPosX,
                     baseY + var15 + window.toggleIconPosY,
                     window.toggleIconSizeX,
                     window.toggleIconSizeY,
                     ColorPalette.text
                  );
                  this.render
                     .drawString(
                        context,
                        window.name,
                        (float)(x + window.toggleIconPosX + window.toggleIconSizeX + 4),
                        (float)(baseY + var15 + window.toggleIconPosY + 1),
                        ColorPalette.text.getRGB(),
                        false
                     );
                  RenderUtil.smoothRoundedRect(
                     context,
                     (double)(x + 190),
                     (double)(baseY + var15 + 14),
                     22.0,
                     12.0,
                     10.0,
                     window.visible ? ApeClient.getRenderColor() : (hover2 ? ColorPalette.mainl37 : ColorPalette.mainl14)
                  );
                  RenderUtil.smoothRoundedRect(
                     context, (double)(x + 192 + (window.visible ? 10 : 0)), (double)(baseY + var15 + 16), 8.0, 8.0, 10.0, ColorPalette.main
                  );
                  var15 += 40;
               }
            }
         }

         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.expanded) {
         int yLevel = 40;

         for (GuiObject object : ApeClient.gui.objects) {
            if (object instanceof OverlayWindow) {
               yLevel += 40;
            }
         }

         int baseY = this.y - (yLevel - 80);
         if (mouseX - (double)this.x > 185.0 && mouseX - (double)this.x < 209.0 && mouseY - (double)baseY > -31.0 && mouseY - (double)baseY < -7.0) {
            this.transparencyTween.setDestinationValue(0.0);
            this.expanded = false;
            return true;
         }

         int var11 = 1;

         for (GuiObject objectx : ApeClient.gui.objects) {
            if (objectx instanceof OverlayWindow) {
               int newY = baseY + var11;
               if (mouseX - (double)this.x > 0.0 && mouseX - (double)this.x < 220.0 && mouseY - (double)newY > 0.0 && mouseY - (double)newY < 40.0) {
                  objectx.visible = !objectx.visible;
               }

               var11 += 40;
            }
         }
      } else if (mouseX - (double)this.x > 191.0 && mouseX - (double)this.x < 215.0 && mouseY - (double)this.y > 8.0 && mouseY - (double)this.y < 32.0) {
         this.transparencyTween.setDestinationValue(153.0);
         this.expanded = true;
         return true;
      }

      return false;
   }

   @Override
   public int getSize() {
      return 40;
   }
}
