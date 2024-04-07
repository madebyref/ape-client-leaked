package net.ape.modules.world;

import java.util.List;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.KeyPollingEvent;
import net.ape.gui.ClickGui;
import net.ape.modules.Module;
import net.minecraft.class_1268;
import net.minecraft.class_1747;
import net.minecraft.class_1755;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_247;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_304;
import net.minecraft.class_3610;
import net.minecraft.class_3612;
import net.minecraft.class_3726;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;

public class MLG extends Module {
   private long placeTime = 0L;
   private class_2338 placePos;
   private List<String> goodItems = List.of(
      "block.minecraft.honey_block",
      "block.minecraft.slime_block",
      "block.minecraft.hay_block",
      "block.minecraft.cobweb",
      "block.minecraft.scaffolding",
      "item.minecraft.water_bucket",
      "item.minecraft.bucket"
   );

   public MLG() {
      super("MLG", ClickGui.world);
   }

   public boolean collidesWithStateAtPos(class_2338 pos, class_2680 state) {
      class_265 voxelShape = state.method_26172(ApeClient.mc.field_1687, pos, class_3726.method_16195(ApeClient.mc.field_1724));
      if (!state.method_26227().method_39360(class_3612.field_15906)) {
         voxelShape = state.method_26227().method_17776(ApeClient.mc.field_1687, pos);
      }

      class_265 voxelShape2 = voxelShape.method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260());
      return class_259.method_1074(voxelShape2, class_259.method_1078(ApeClient.mc.field_1724.method_5829()), class_247.field_16896);
   }

   @EventListener
   public void actionTick(KeyPollingEvent event) {
      if (ApeClient.mc.field_1724 != null
         && ApeClient.mc.field_1724.field_3913 != null
         && ApeClient.mc.field_1765 != null
         && this.placeTime < System.currentTimeMillis()
         && !ApeClient.mc.field_1724.method_31549().field_7478) {
         class_1799 hand = ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_5998(class_1268.field_5808) : new class_1799(class_1802.field_8162);
         class_1792 item = hand.method_7909();
         if (this.goodItems.contains(item.method_7876())) {
            String var4 = item.method_7876();
            switch (var4) {
               case "item.minecraft.bucket":
                  class_239 hit = ApeClient.mc.method_1560().method_5745(6.0, 0.0F, true);
                  if (hit.method_17783() == class_240.field_1332) {
                     class_2338 blockPos = ((class_3965)hit).method_17777();
                     class_3610 fluidState = ApeClient.mc.field_1687.method_8316(blockPos);
                     if (fluidState != null && blockPos.equals(this.placePos)) {
                        this.placePos = null;
                        this.placeTime = System.currentTimeMillis() + 200L;
                        class_304.method_1420(ApeClient.mc.field_1690.field_1904.method_1429());
                     }
                  }
                  break;
               case "block.minecraft.scaffolding":
                  if (!ApeClient.mc.field_1724.method_5715()) {
                     break;
                  }
               default:
                  if (ApeClient.mc.field_1765.method_17783() == class_240.field_1332 && ApeClient.mc.field_1724.field_6017 > 3.3F) {
                     class_3965 blockHitResult = (class_3965)ApeClient.mc.field_1765;
                     class_2338 blockPos = blockHitResult.method_17777().method_10081(blockHitResult.method_17780().method_10163());
                     if (this.collidesWithStateAtPos(
                           new class_2338(blockPos.method_10263(), (int)ApeClient.mc.field_1724.method_23318(), blockPos.method_10260()),
                           (item instanceof class_1755 ? class_2246.field_10382 : ((class_1747)item).method_7711()).method_9564()
                        )
                        && ApeClient.mc.field_1724.method_19538().method_10214() - (double)blockPos.method_10264() > 0.0) {
                        class_2248 block = ApeClient.mc.field_1687.method_8320(blockPos).method_26204();
                        if (block == class_2246.field_10124 || block == class_2246.field_10543) {
                           this.placePos = blockPos;
                           this.placeTime = System.currentTimeMillis() + 200L;
                           class_304.method_1420(ApeClient.mc.field_1690.field_1904.method_1429());
                        }
                     }
                  }
            }
         }
      }
   }
}
