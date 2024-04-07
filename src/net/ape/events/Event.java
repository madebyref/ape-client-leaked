package net.ape.events;

public class Event {
   boolean canceled = false;

   public void cancel() {
      this.canceled = true;
   }

   public boolean isCanceled() {
      return this.canceled;
   }
}
