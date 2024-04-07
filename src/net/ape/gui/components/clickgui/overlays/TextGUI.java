package net.ape.gui.components.clickgui.overlays;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import net.ape.ApeClient;
import net.ape.gui.components.clickgui.Dropdown;
import net.ape.gui.components.clickgui.OverlayWindow;
import net.ape.gui.components.clickgui.TextBox;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.handlers.ModuleHandler;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_124;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class TextGUI extends OverlayWindow {
   Dropdown sort = new Dropdown("Sort", this, Arrays.asList("Alphabetical", "Length"), null);
   Toggle shadow = new Toggle("Shadow", this, null);
   Toggle watermark = new Toggle("Watermark", this, null);
   Toggle bkg = new Toggle("Render background", this, null);
   Toggle customTextToggle = new Toggle("Add custom text", this, callback -> this.customText.visible = (Boolean)callback);
   TextBox customText = new TextBox("Custom text", this);
   class_2960 logo = new class_2960("ape/textape.png");
   class_2960 v4 = new class_2960("ape/textv4.png");
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 14");
   TTFFontRenderer render2 = CustomFont.FONT_MANAGER.getFont("Arial 25");

   public TextGUI() {
      super("Text GUI", new class_2960("ape/textguitab.png"), 21, 16, new class_2960("ape/textguiicon.png"), 16, 12, 12, 14);
      this.customText.visible = false;
   }

   @Override
   public void customRender(int yAdd, class_332 context) {
      boolean right = (float)(this.x + 110) > (float)ApeClient.mc.method_22683().method_4489() / 2.0F;
      Color mainGuiColor = ApeClient.getRenderColor();
      if (this.watermark.enabled) {
         if (this.shadow.enabled) {
            RenderUtil.image(context, this.logo, this.x + (right ? 127 : 6) + 1, this.y + yAdd + 7, 58, 21, new Color(0, 0, 0, 115));
            RenderUtil.image(context, this.v4, this.x + (right ? 127 : 6) + 62, this.y + yAdd + 8, 33, 18, new Color(0, 0, 0, 115));
         }

         RenderUtil.image(context, this.logo, this.x + (right ? 127 : 6), this.y + yAdd + 6, 58, 21, mainGuiColor);
         RenderUtil.image(context, this.v4, this.x + (right ? 127 : 6) + 61, this.y + yAdd + 7, 33, 18);
         yAdd += 21;
      }

      if (this.customTextToggle.enabled) {
         if (this.shadow.enabled) {
            this.render2
               .drawString(
                  context,
                  this.customText.value,
                  (float)this.x + (right ? 222.0F - this.render2.getWidth(this.customText.value) : 6.0F) + 1.0F,
                  (float)(this.y + yAdd + 12),
                  new Color(0, 0, 0, 115).getRGB(),
                  false
               );
         }

         this.render2
            .drawString(
               context,
               this.customText.value,
               (float)this.x + (right ? 222.0F - this.render2.getWidth(this.customText.value) : 6.0F),
               (float)(this.y + yAdd + 11),
               mainGuiColor.getRGB(),
               false
            );
         yAdd += 28;
      }

      ArrayList<String> enabledModules = Lists.newArrayList();

      for (Module mod : ModuleHandler.modules) {
         if (mod.enabled) {
            enabledModules.add(mod.name + (mod.extra().equals("") ? "" : class_124.field_1080 + mod.extra()));
         }
      }

      if (this.sort.value.equals("Alphabetical")) {
         enabledModules.sort(Comparator.comparing(o -> (String)o));
      } else {
         enabledModules.sort(Comparator.comparing(o -> 500.0F - this.render.getWidth(o)));
      }

      int i = 0;

      for (String str : enabledModules) {
         i++;
         float xPos = (float)this.x + (right ? 217.0F - this.render.getWidth(str) : 6.0F);
         float yPos = (float)(this.y + yAdd - 2 + i * (this.bkg.enabled ? 20 : 18));
         Color guiColor = ApeClient.getRenderColor((float)i * -0.025F);
         if (this.bkg.enabled) {
            float width = this.render.getWidth(str) + 7.0F;
            RenderUtil.rect(context, xPos - 3.0F + (float)(right ? 0 : -1), yPos - 4.0F, width, 20.0F, new Color(0, 0, 0, 128));
            RenderUtil.rect(context, xPos - 3.0F + (float)(right ? 0 : -1), yPos - 4.0F + 19.0F, width, 1.0F, new Color(0, 0, 0, 18));
            if (i != 1) {
               RenderUtil.rect(context, xPos - 3.0F + (float)(right ? 0 : -1), yPos - 4.0F, width, 1.0F, new Color(0, 0, 0, 18));
            }

            RenderUtil.rect(context, xPos - 3.0F + (right ? width : -3.0F), yPos - 4.0F, 2.0F, 20.0F, guiColor);
         }

         this.render.drawString(context, str, xPos, yPos, guiColor.getRGB(), this.shadow.enabled);
      }
   }
}
