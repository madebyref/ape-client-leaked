package net.ape.events;

public class VelocityEvent extends Event {
   public VelocityEvent.VelocityType velocityType;
   public double x;
   public double y;
   public double z;

   public VelocityEvent(double x, double y, double z, VelocityEvent.VelocityType velocityType) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.velocityType = velocityType;
   }

   public static enum VelocityType {
      NORMAL,
      EXPLOSION;
   }
}
