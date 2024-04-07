package net.ape.events;

public class PlayerMoveEvent extends Event {
   public float yaw;
   public float pitch;
   public boolean modified;
   public boolean movementCorrection;
   public int customInputX;
   public int customInputZ;

   public PlayerMoveEvent(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }
}
