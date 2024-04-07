package net.ape.gui.components;

import java.awt.Color;
import net.minecraft.class_3532;

public class ColorTween {
   long timeFrame = 0L;
   int timeTaken = 0;
   Color destinationValue;
   Color startValue;
   boolean finished = false;

   public ColorTween(double timeVal, Color start, Color dest) {
      this.destinationValue = dest;
      this.startValue = start;
      this.timeTaken = (int)(timeVal * 1000.0);
      this.timeFrame = System.currentTimeMillis() + (long)this.timeTaken;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public Color getValue() {
      float delta = class_3532.method_15363((float)(this.timeFrame - System.currentTimeMillis()) / (float)this.timeTaken, 0.0F, 1.0F);
      return new Color(
         class_3532.method_15340(this.destinationValue.getRed() + (int)((float)(this.startValue.getRed() - this.destinationValue.getRed()) * delta), 0, 255),
         class_3532.method_15340(
            this.destinationValue.getGreen() + (int)((float)(this.startValue.getGreen() - this.destinationValue.getGreen()) * delta), 0, 255
         ),
         class_3532.method_15340(this.destinationValue.getBlue() + (int)((float)(this.startValue.getBlue() - this.destinationValue.getBlue()) * delta), 0, 255)
      );
   }

   public void setDestinationValue(Color dest, boolean done) {
      if (this.destinationValue.getRGB() != dest.getRGB()) {
         float delta = class_3532.method_15363((float)(this.timeFrame - System.currentTimeMillis()) / (float)this.timeTaken, 0.0F, 1.0F);
         if (done) {
            this.startValue = this.getValue();
            this.timeFrame = System.currentTimeMillis() + (long)this.timeTaken;
         }

         this.destinationValue = dest;
      }
   }
}
