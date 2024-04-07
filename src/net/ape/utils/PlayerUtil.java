package net.ape.utils;

import net.ape.ApeClient;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_266;
import net.minecraft.class_267;
import net.minecraft.class_269;
import net.minecraft.class_8646;

public class PlayerUtil {
   public static boolean isTargetable(class_1297 ent, boolean friendCheck, boolean botCheck, boolean teamCheck) {
      return botCheck && class_124.method_539(ent.method_5820()) == ""
         ? false
         : ApeClient.mc.field_1724 != null && (!teamCheck || !ApeClient.mc.field_1724.method_5722(ent));
   }

   public static double getHealth(class_1309 ent, boolean bypass) {
      if (bypass && ent instanceof class_1657 playerEntity) {
         class_269 board = ApeClient.mc.field_1687 != null ? ApeClient.mc.field_1687.method_8428() : null;
         class_266 obj = board != null ? board.method_1189(class_8646.field_45156) : null;
         class_267 score = obj != null ? board.method_1180(playerEntity.method_7334().getName(), obj) : null;
         if (score != null) {
            return (double)score.method_1126();
         }
      }

      return (double)(ent.method_6032() + ent.method_6067());
   }

   public static double getHealth(class_1309 ent) {
      return getHealth(ent, false);
   }
}
