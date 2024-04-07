package net.ape.events;

import net.minecraft.class_2547;
import net.minecraft.class_2596;

public class PacketEvent extends Event {
   public PacketEvent.PacketType packetType;
   public class_2596 packet;
   public class_2547 net;

   public PacketEvent(class_2596 packet, PacketEvent.PacketType type, class_2547 net) {
      this.packet = packet;
      this.packetType = type;
      this.net = net;
   }

   public static enum PacketType {
      RECEIVE,
      SENT;
   }
}
