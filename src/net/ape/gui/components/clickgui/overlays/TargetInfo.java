package net.ape.gui.components.clickgui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.ape.gui.components.ColorPalette;
import net.ape.gui.components.ColorTween;
import net.ape.gui.components.Tween;
import net.ape.gui.components.clickgui.ColorSlider;
import net.ape.gui.components.clickgui.OverlayWindow;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.utils.PlayerUtil;
import net.ape.utils.RenderUtil;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_490;
import net.minecraft.class_745;

public class TargetInfo extends OverlayWindow {
   Toggle bkg = new Toggle("Render Background", this, callback -> {
      if (this.bkgTrans != null) {
         this.bkgTrans.visible = (Boolean)callback;
      }
   }, true);
   Slider bkgTrans = new Slider("Transparency", this, 0, 10, 5, 10.0);
   Toggle bkgColorToggle = new Toggle("Background Color", this, callback -> this.bkgColor.visible = (Boolean)callback);
   ColorSlider bkgColor = new ColorSlider("Color", this);
   Toggle HealthBypass = new Toggle("Health Bypass", this, null);
   class_2960 blur = new class_2960("ape/blur.png");
   TTFFontRenderer render14 = CustomFont.FONT_MANAGER.getFont("Arial 14");
   TTFFontRenderer render16 = CustomFont.FONT_MANAGER.getFont("Arial 16");
   ColorTween healthColor = new ColorTween(0.3, Color.getHSBColor(0.4F, 0.89F, 0.75F), Color.getHSBColor(0.4F, 0.89F, 0.75F));
   Tween healthSize = new Tween(0.3, 138.0, 138.0);
   public class_1309 target;
   public long targetTime = System.currentTimeMillis();

   public TargetInfo() {
      super("Target Info", new class_2960("ape/targetinfotab.png"), 16, 16, new class_2960("ape/targetinfoicon.png"), 14, 14, 12, 14);
      this.bkgColor.visible = false;
   }

   @Override
   public void customRender(int yAdd, class_332 context) {
      if (this.targetTime >= System.currentTimeMillis() || yAdd != 0) {
         Color bColor = Color.getHSBColor((float)this.bkgColor.value / 100.0F, (float)this.bkgColor.sat.value / 100.0F, (float)this.bkgColor.val.value / 100.0F);
         Color main = this.bkgColorToggle.enabled ? bColor : ColorPalette.main;
         if (this.bkg.enabled) {
            Color bkgVar = this.bkgColorToggle.enabled ? bColor : ColorPalette.maind1;
            RenderUtil.roundedRect(
               context,
               (double)this.x,
               (double)(this.y + yAdd),
               220.0,
               81.0,
               5.0,
               new Color(
                  (float)bkgVar.getRed() / 255.0F, (float)bkgVar.getGreen() / 255.0F, (float)bkgVar.getBlue() / 255.0F, (float)this.bkgTrans.value / 10.0F
               )
            );
            RenderSystem.enableBlend();
            context.method_25290(this.blur, this.x - 48, this.y + yAdd - 31, 0.0F, 0.0F, 309, 45, 309, 533);
            context.method_25293(this.blur, this.x - 48, this.y + yAdd + 14, 309, 61, 0.0F, 45.0F, 309, 1, 309, 533);
            context.method_25293(this.blur, this.x - 48, this.y + yAdd + 77 - 2, 309, 45, 0.0F, 506.0F, 309, 45, 309, 533);
         }

         if (this.target != null) {
            double health = PlayerUtil.getHealth(this.target, this.HealthBypass.enabled);
            health = Math.min(health, (double)this.target.method_6063());
            double percent = health / (double)this.target.method_6063();
            this.healthColor.setDestinationValue(Color.getHSBColor(class_3532.method_15363((float)(percent / 2.5), 0.0F, 1.0F), 0.89F, 0.75F), true);
            this.healthSize.setDestinationValue(138.0 * percent);
            if (this.target instanceof class_745) {
               RenderUtil.image(context, ((class_745)this.target).method_52814().comp_1626(), this.x + 9, this.y + yAdd + 14, 8, 8, 8, 8, 52, 53, 64, 64);
            } else {
               RenderUtil.roundedRect(context, (double)(this.x + 9), (double)(this.y + yAdd + 14), 52.0, 53.0, 5.0, main);
               class_238 bb = this.target.method_5829();
               double size = Math.sqrt(
                  (bb.field_1320 - bb.field_1323) * (bb.field_1320 - bb.field_1323)
                     + (bb.field_1325 - bb.field_1322) * (bb.field_1325 - bb.field_1322)
                     + (bb.field_1324 - bb.field_1321) * (bb.field_1324 - bb.field_1321)
               );
               class_490.method_2486(
                  context, this.x + 35, this.y + yAdd + 65, this.x + 400, this.y + yAdd + 400, (int)(50.0 / size), 0.0F, 0.0F, 0.0F, this.target
               );
            }

            (this.bkg.enabled ? this.render14 : this.render16)
               .drawString(
                  context,
                  this.target.method_5477().getString(),
                  (float)(this.x + 70),
                  (float)(this.y + yAdd + 13),
                  (this.bkg.enabled ? ColorPalette.textd16 : Color.white).getRGB(),
                  true
               );
            RenderUtil.roundedRect(context, (double)(this.x + 72), (double)(this.y + yAdd + 32), 138.0, (double)(this.bkg.enabled ? 4 : 7), 5.0, main);
            if (this.healthSize.getValue() > 5) {
               RenderUtil.roundedRect(
                  context,
                  (double)(this.x + 72),
                  (double)(this.y + yAdd + 32),
                  (double)this.healthSize.getValue(),
                  (double)(this.bkg.enabled ? 4 : 7),
                  5.0,
                  this.healthColor.getValue()
               );
            }

            int plus = 0;

            for (int i = 1; i < 6; i++) {
               RenderUtil.roundedRect(context, (double)(this.x + 72 + plus), (double)(this.y + yAdd + 44), i % 3 == 0 ? 26.0 : 25.0, 27.0, 5.0, main);
               if (this.target instanceof class_745) {
               }

               plus += i % 3 == 0 ? 28 : 27;
            }
         }
      }
   }
}
