package net.ape.events;

public class CameraUpdateEvent extends Event {
   public float timePassed = 0.0F;
   public int deltaX = 0;
   public int deltaY = 0;

   public CameraUpdateEvent(float timePassed, int deltaX, int deltaY) {
      this.timePassed = timePassed;
      this.deltaX = deltaX;
      this.deltaY = deltaY;
   }
}
