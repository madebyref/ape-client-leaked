package net.ape.gui.components;

import net.minecraft.class_3532;

public class Tween {
   long timeFrame = 0L;
   int timeTaken = 0;
   double destinationValue = 0.0;
   double startValue = 0.0;
   boolean finished = false;
   boolean expo = false;
   boolean first = true;

   public Tween(double timeVal, double start, double dest) {
      this.destinationValue = dest;
      this.startValue = start;
      this.timeTaken = (int)(timeVal * 1000.0);
      this.timeFrame = System.currentTimeMillis() + (long)this.timeTaken;
   }

   public Tween(double timeVal, double start, double dest, boolean expo) {
      this(timeVal, start, dest);
      this.expo = expo;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public int getValue() {
      float change = class_3532.method_15363((float)(this.timeFrame - System.currentTimeMillis()) / (float)this.timeTaken, 0.0F, 1.0F);
      this.finished = change == 0.0F;
      return this.expo
         ? (int)((this.startValue - this.destinationValue) * (double)((float)Math.pow(2.0, (double)(10.0F * (change - 1.0F)))) + this.destinationValue)
         : (int)(this.destinationValue + (this.startValue - this.destinationValue) * (double)change);
   }

   public void setDestinationValue(double dest) {
      if (this.destinationValue != dest) {
         this.startValue = (double)this.getValue();
         this.destinationValue = dest;
         this.timeFrame = System.currentTimeMillis() + (long)this.timeTaken;
      }
   }

   public void setDestinationValueFirst(double dest) {
      if (this.destinationValue != dest) {
         this.startValue = (double)this.getValue();
         this.destinationValue = dest;
         this.timeFrame = System.currentTimeMillis() + (long)(this.first ? 0 : this.timeTaken);
         this.first = false;
      }
   }
}
