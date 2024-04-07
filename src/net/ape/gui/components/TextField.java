package net.ape.gui.components;

import net.ape.ApeClient;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.RenderUtil;
import net.minecraft.class_155;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class TextField {
   public boolean focused;
   public String value;
   String placeholder;
   boolean numbersOnly;
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 12");

   public TextField(String placeholder, boolean numbersOnly) {
      this.placeholder = placeholder;
      this.numbersOnly = numbersOnly;
   }

   public TextField(boolean numbersOnly) {
      this("Click to set", numbersOnly);
   }

   public void render(class_332 context, int x, int y) {
      String text = this.value.equals("") && !this.focused ? this.placeholder : this.value;
      this.render.drawString(context, text, (float)x, (float)y, (this.value.equals("") ? ColorPalette.textd31 : ColorPalette.textd16).getRGB(), false);
      if (this.focused && System.currentTimeMillis() % 1000L < 500L) {
         RenderUtil.rect(context, (float)(x + 2) + this.render.getWidth(text), (float)(y - 2), 2.0F, 14.0F, ColorPalette.textd43);
      }
   }

   public void charTyped(char typedChar) {
      if (class_155.method_643(typedChar)) {
         if (this.numbersOnly && !Character.toString(typedChar).matches("[0-9|.|-]+")) {
            return;
         }

         this.value = this.value + typedChar;
      }
   }

   public void keyPressed(int keyCode) {
      if (this.focused) {
         if (class_437.method_25438(keyCode)) {
            ApeClient.mc.field_1774.method_1455(this.value);
         } else if (class_437.method_25437(keyCode)) {
            this.value = class_155.method_644(ApeClient.mc.field_1774.method_1460());
         } else {
            if ((keyCode == 259 || keyCode == 261) && this.value.length() > 0) {
               this.value = this.value.substring(0, this.value.length() - 1);
            }
         }
      }
   }
}
