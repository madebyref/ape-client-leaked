package net.ape.gui.components;

import java.awt.Color;

public class ColorPalette {
   public static Color main = new Color(26, 25, 26);
   public static Color text = new Color(200, 200, 200);
   public static Color mainl02 = light(main, 0.02);
   public static Color mainl034 = light(main, 0.034);
   public static Color mainl04 = light(main, 0.04);
   public static Color mainl05 = light(main, 0.05);
   public static Color mainl0875 = light(main, 0.0875);
   public static Color mainl1 = light(main, 0.1);
   public static Color mainl14 = light(main, 0.14);
   public static Color mainl37 = light(main, 0.37);
   public static Color maind02 = dark(main, 0.02);
   public static Color maind1 = dark(main, 0.1);
   public static Color textd16 = dark(text, 0.16);
   public static Color textd31 = dark(text, 0.31);
   public static Color textd43 = dark(text, 0.43);
   public static Color textl2 = light(text, 0.2);

   public static Color dark(Color color, double num) {
      float[] vals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      float compare = Color.RGBtoHSB(main.getRed(), main.getGreen(), main.getBlue(), null)[2];
      return Color.getHSBColor(vals[0], vals[1], (float)Math.min(Math.max((double)compare > 0.5 ? (double)vals[2] + num : (double)vals[2] - num, 0.0), 1.0));
   }

   public static Color light(Color color, double num) {
      float[] vals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
      float compare = Color.RGBtoHSB(main.getRed(), main.getGreen(), main.getBlue(), null)[2];
      return Color.getHSBColor(vals[0], vals[1], (float)Math.min(Math.max((double)compare > 0.5 ? (double)vals[2] - num : (double)vals[2] + num, 0.0), 1.0));
   }
}
