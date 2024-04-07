package net.ape.gui.components.clickgui;

import java.awt.Color;
import net.ape.ApeClient;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.TextField;
import net.ape.gui.components.Tween;
import net.ape.gui.components.clickgui.settings.GUIColorSlider;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class ColorSlider extends GuiObject {
   String name;
   int min;
   int max;
   public int value;
   double decimal;
   boolean dragging;
   String type;
   TTFFontRenderer render = CustomFont.FONT_MANAGER.getFont("Arial 12");
   class_2960 color = new class_2960("ape/colorpreview.png");
   class_2960 uparrow = new class_2960("ape/expandup.png");
   class_2960 downarrow = new class_2960("ape/expanddown.png");
   class_2960 rain1 = new class_2960("ape/rainbow_1.png");
   class_2960 rain2 = new class_2960("ape/rainbow_2.png");
   class_2960 rain3 = new class_2960("ape/rainbow_3.png");
   class_2960 rain4 = new class_2960("ape/rainbow_4.png");
   public Tween positionTween = new Tween(0.2, 185.0, 185.0);
   Tween sizeTween = new Tween(0.1, 14.0, 16.0);
   TextField field = new TextField(false);
   public ColorSlider sat;
   public ColorSlider val;
   GuiObject parent;
   boolean expanded = false;
   long clickTick = System.currentTimeMillis();

   public ColorSlider(String name, GuiObject mod, String type, @Nullable GuiObject parent) {
      this.name = name;
      this.min = 0;
      this.max = 100;
      this.decimal = 100.0;
      this.type = type;
      this.parent = (GuiObject)(parent != null ? parent : this);
      this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
      mod.objects.add(this);
      if (type.equals("Color")) {
         this.sat = new ColorSlider(name + "Saturation", mod, "Sat", this);
         this.sat.visible = false;
         this.val = new ColorSlider(name + "Value", mod, "Val", this);
         this.val.visible = false;
      }
   }

   public ColorSlider(String name, GuiObject mod, String type) {
      this(name, mod, type, null);
   }

   public ColorSlider(String name, Module mod) {
      this(name, mod.button, "Color", null);
   }

   public ColorSlider(String name, GuiObject mod) {
      this(name, mod, "Color", null);
   }

   public void toggleRainbow() {
      if (ApeClient.rainbowQueue.contains(this)) {
         ApeClient.rainbowQueue.remove(this);
      } else {
         ApeClient.rainbowQueue.add(this);
      }
   }

   @Override
   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (super.render(mouseX, mouseY, context, x, y, ind)) {
         return false;
      } else {
         if (this.dragging) {
            this.value = (int)((float)this.min + (float)(this.max - this.min) * class_3532.method_15363((float)(mouseX - (x + 15)) / 200.0F, 0.0F, 1.0F));
            this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
            if (this.parent instanceof GUIColorSlider newParent) {
               newParent.custom = true;
               newParent.value = 3;
               newParent.updateSlider(false);
            }
         }

         this.render
            .drawString(
               context,
               this.parent == this ? this.name : (this.type.equals("Color") ? "Custom Color" : (this.type.equals("Sat") ? "Saturation" : "Vibrance")),
               (float)(x + 9),
               (float)(y + 11),
               ColorPalette.textd16.getRGB(),
               false
            );
         Color newColor;
         if (this.parent instanceof ColorSlider newParent) {
            newColor = Color.getHSBColor((float)newParent.value / 100.0F, (float)newParent.sat.value / 100.0F, (float)newParent.val.value / 100.0F);
         } else {
            GUIColorSlider newParent = (GUIColorSlider)this.parent;
            newColor = Color.getHSBColor((float)newParent.hue.value / 100.0F, (float)newParent.sat.value / 100.0F, (float)newParent.val.value / 100.0F);
         }

         if (this.field.focused) {
            this.field.render(context, x + 190, y + 11);
         } else if (this.parent == this) {
            RenderUtil.image(context, this.color, x + 198, y + 10, 12, 12, newColor);
         }

         boolean hovered = mouseX - x > 0 && mouseX - x < 220 && mouseY - y > 0 && mouseY - y < 50;
         if (this.parent == this) {
            RenderUtil.image(
               context, this.expanded ? this.uparrow : this.downarrow, x + 12 + (int)this.render.getWidth(this.name), y + 11, 9, 4, ColorPalette.textd31
            );
            boolean inRainbow = ApeClient.rainbowQueue.contains(this);
            RenderUtil.image(context, this.rain1, x + 178, y + 10, 12, 12, inRainbow ? new Color(5, 127, 100) : ColorPalette.mainl37);
            RenderUtil.image(context, this.rain2, x + 178, y + 10, 12, 12, inRainbow ? new Color(228, 125, 43) : ColorPalette.mainl37);
            RenderUtil.image(context, this.rain3, x + 178, y + 10, 12, 12, inRainbow ? new Color(225, 46, 52) : ColorPalette.mainl37);
            RenderUtil.image(context, this.rain4, x + 178, y + 10, 12, 12, ColorPalette.mainl37);
         }

         if (this.type.equals("Color")) {
            for (int i = 0; i < 10; i++) {
               if (this.type.equals("Color")) {
                  Color.getHSBColor((float)i * 0.1F, 1.0F, 1.0F);
               } else {
                  Color var10000 = this.type.equals("Sat") ? Color.white : Color.black;
               }

               if (this.type.equals("Color")) {
                  Color.getHSBColor((float)(i + 1) * 0.1F, 1.0F, 1.0F);
               }

               RenderUtil.gradientSideways(
                  context, x + 10 + i * 20, y + 37, 20, 2, Color.getHSBColor((float)i * 0.1F, 1.0F, 1.0F), Color.getHSBColor((float)(i + 1) * 0.1F, 1.0F, 1.0F)
               );
            }
         } else {
            float val = 0.0F;
            if (this.parent instanceof ColorSlider) {
               val = (float)((ColorSlider)this.parent).val.value / 100.0F;
            } else {
               val = (float)((GUIColorSlider)this.parent).val.value / 100.0F;
            }

            RenderUtil.gradientSideways(context, x + 10, y + 37, 200, 2, this.type.equals("Sat") ? Color.getHSBColor(0.0F, 0.0F, val) : Color.black, newColor);
         }

         float width = (float)this.positionTween.getValue();
         RenderUtil.rect(
            context, (float)(x + 5) + width, (float)(y + 28), 22.0F, 20.0F, this.parent instanceof GUIColorSlider ? ColorPalette.main : ColorPalette.maind02
         );
         this.sizeTween.setDestinationValue(hovered ? 16.0 : 14.0);
         RenderUtil.roundedRect(
            context,
            (double)((float)x + (16.0F - (float)this.sizeTween.getValue() / 2.0F) + width),
            (double)((float)(y + 30) + (8.0F - (float)this.sizeTween.getValue() / 2.0F)),
            (double)this.sizeTween.getValue(),
            (double)this.sizeTween.getValue(),
            (double)(this.sizeTween.getValue() + 2),
            ColorPalette.text
         );
         return true;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.visible) {
         if (this.parent == this) {
            int check = this.x + 12 + (int)this.render.getWidth(this.name) - 4;
            if (mouseX - (double)check > 0.0 && mouseX - (double)check < 17.0 && mouseY - (double)this.y > 7.0 && mouseY - (double)this.y < 19.0 && button == 0
               )
             {
               this.expanded = !this.expanded;
               this.sat.visible = this.expanded;
               this.val.visible = this.expanded;
               return true;
            }

            check = this.x + 178;
            if (mouseX - (double)check > 0.0
               && mouseX - (double)check < 12.0
               && mouseY - (double)this.y > 10.0
               && mouseY - (double)this.y < 22.0
               && button == 0) {
               this.toggleRainbow();
               return true;
            }

            if (mouseX - (double)this.x > 160.0
               && mouseX - (double)this.x < 220.0
               && mouseY - (double)this.y > 0.0
               && mouseY - (double)this.y < 20.0
               && button == 0) {
               ColorSlider newParent = (ColorSlider)this.parent;
               Color newColor = Color.getHSBColor((float)newParent.value / 100.0F, (float)newParent.sat.value / 100.0F, (float)newParent.val.value / 100.0F);
               this.field.value = newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue();
               this.field.focused = true;
               return true;
            }
         }

         if (mouseX - (double)this.x > 0.0
            && mouseX - (double)this.x < 220.0
            && mouseY - (double)this.y > 20.0
            && mouseY - (double)this.y < 50.0
            && button == 0) {
            if (System.currentTimeMillis() - this.clickTick < 300L && this.parent == this) {
               this.toggleRainbow();
            }

            this.clickTick = System.currentTimeMillis();
            this.dragging = true;
            return true;
         }

         this.field.focused = false;
      }

      return false;
   }

   @Override
   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.dragging = false;
      return false;
   }

   @Override
   public void keyTyped(int keyCode) {
      if (this.field.focused) {
         this.field.keyPressed(keyCode);
         if (keyCode == 257 || keyCode == 335) {
            try {
               String[] strs = this.field.value.split(",");
               Color newColor;
               if (strs.length > 1 && Integer.valueOf(strs[0]) != null) {
                  newColor = new Color(Integer.valueOf(strs[0]), Integer.valueOf(strs[1].replace(" ", "")), Integer.valueOf(strs[2].replace(" ", "")));
               } else {
                  newColor = new Color(Integer.parseInt(this.field.value.replace("#", ""), 16));
               }

               if (newColor != null) {
                  float[] vals = Color.RGBtoHSB(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), null);
                  this.value = Math.round(vals[0] * 100.0F);
                  this.sat.value = Math.round(vals[1] * 100.0F);
                  this.val.value = Math.round(vals[2] * 100.0F);
                  this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / 100.0F), 0.0F, 185.0F));
                  this.sat.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.sat.value / 100.0F), 0.0F, 185.0F));
                  this.val.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.val.value / 100.0F), 0.0F, 185.0F));
               }
            } catch (Exception var5) {
            }

            this.field.focused = false;
         }
      }
   }

   @Override
   public void charTyped(char key) {
      if (this.field.focused) {
         this.field.charTyped(key);
      }
   }

   @Override
   public int getSize() {
      return 50;
   }

   @Override
   public void save(JSONObject saved) {
      if (this.type.equals("Color")) {
         JSONObject data = new JSONObject();
         data.put("value", this.value);
         data.put("rainbow", ApeClient.rainbowQueue.contains(this));
         saved.put(this.name, data);
      } else {
         saved.put(this.name, this.value);
      }
   }

   @Override
   public void load(JSONObject saved) {
      if (saved.has(this.name)) {
         if (this.type.equals("Color") && saved.get(this.name) instanceof JSONObject) {
            JSONObject data = saved.getJSONObject(this.name);
            this.value = data.getInt("value");
            if (ApeClient.rainbowQueue.contains(this) != data.getBoolean("rainbow")) {
               this.toggleRainbow();
            }

            this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
         } else {
            this.value = saved.getInt(this.name);
            this.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)this.value / (float)this.max), 0.0F, 185.0F));
         }
      }
   }
}
