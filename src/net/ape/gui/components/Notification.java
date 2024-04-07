package net.ape.gui.components;

import java.awt.Color;
import net.ape.gui.font.CustomFont;
import net.minecraft.class_2960;

public class Notification {
   public String name;
   public String text;
   public Tween positionTween;
   public Tween positionYTween;
   public Tween barTween;
   public long time;
   public int sizeX;
   public Color barColor;
   public class_2960 icon;

   public Notification(String name, String text, double time, String type) {
      this.name = name;
      this.text = text;
      this.time = System.currentTimeMillis() + (long)((int)((time + 0.2) * 1000.0));
      this.sizeX = (int)Math.max(CustomFont.FONT_MANAGER.getFont("Arial 14").getWidth(text) + 80.0F, 276.0F);
      this.positionTween = new Tween(0.4, 0.0, (double)this.sizeX, true);
      this.positionYTween = new Tween(0.4, 0.0, 0.0, true);
      this.barTween = new Tween(time, 276.0, 0.0);
      this.icon = new class_2960("ape/" + type + ".png");
      this.barColor = type.equals("alert") ? new Color(250, 50, 56) : (type.equals("warning") ? new Color(236, 129, 43) : new Color(220, 220, 220));
   }
}
