package net.ape.modules.blatant;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.ape.ApeClient;
import net.ape.events.CameraUpdateEvent;
import net.ape.events.EventListener;
import net.ape.events.JumpEvent;
import net.ape.events.KeyPollingEvent;
import net.ape.events.PlayerMoveEvent;
import net.ape.gui.ClickGui;
import net.ape.gui.components.Tween;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.mixins.MinecraftClientAccessor;
import net.ape.modules.Module;
import net.ape.utils.PlayerUtil;
import net.minecraft.class_124;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1322;
import net.minecraft.class_1657;
import net.minecraft.class_1675;
import net.minecraft.class_1743;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_5134;

public class Killaura extends Module {
   public Slider swingRange = new Slider("Swing range", this, 1, 60, 30, 10.0);
   public Slider attackRange = new Slider("Attack range", this, 1, 60, 30, 10.0);
   private Slider aimSpeed = new Slider("Aim speed", this, 1, 200, 100, 100.0);
   public Slider maxAngle = new Slider("Max angle", this, 1, 360, 360, 1.0);
   private final Toggle wallCheck = new Toggle("Wall Check", this, null);
   private final Toggle swordOnly = new Toggle("Limit to sword", this, null);
   boolean isAttacking;
   class_1297 lastTarget;
   float auraYaw;
   float auraPitch;
   boolean goodJump;
   static Tween xTween = new Tween(0.2, 0.0, 0.0);
   static Tween yTween = new Tween(0.2, 0.0, 0.0);
   static Tween zTween = new Tween(0.2, 0.0, 0.0);
   boolean returning = false;

   public Killaura() {
      super("Killaura", ClickGui.blatant);
   }

   @EventListener
   public void jumpTick(JumpEvent event) {
      this.goodJump = true;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.isAttacking = false;
      this.returning = false;
   }

   private final class_243 getRotationVector(float pitch, float yaw) {
      float f = pitch * (float) (Math.PI / 180.0);
      float g = -yaw * (float) (Math.PI / 180.0);
      float h = class_3532.method_15362(g);
      float i = class_3532.method_15374(g);
      float j = class_3532.method_15362(f);
      float k = class_3532.method_15374(f);
      return new class_243((double)(i * j), (double)(-k), (double)(h * j));
   }

   public class_1297 getViewEntity(float yaw, float pitch) {
      class_1297 entity2 = ApeClient.mc.method_1560();
      if (entity2 == null) {
         return null;
      } else if (ApeClient.mc.field_1687 == null) {
         return null;
      } else {
         double d = 6.0;
         class_243 vec3d = entity2.method_5836(1.0F);
         boolean bl = false;
         int i = 3;
         double e = d;
         if (ApeClient.mc.field_1761.method_2926()) {
            e = 6.0;
            d = 6.0;
         } else {
            if (d > 3.0) {
               bl = true;
            }

            d = d;
         }

         e *= e;
         class_243 vec3d2 = this.getRotationVector(pitch, yaw);
         class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * d, vec3d2.field_1351 * d, vec3d2.field_1350 * d);
         float f = 1.0F;
         class_238 box = entity2.method_5829().method_18804(vec3d2.method_1021(d)).method_1009(1.0, 1.0, 1.0);
         class_3966 entityHitResult = class_1675.method_18075(entity2, vec3d, vec3d3, box, entity -> !entity.method_7325() && entity.method_5863(), e);
         if (entityHitResult != null) {
            class_1297 entity22 = entityHitResult.method_17782();
            class_243 vec3d4 = entityHitResult.method_17784();
            double g = vec3d.method_1025(vec3d4);
            double reach = (double)this.attackRange.value / 10.0;
            if (bl && g > reach * reach) {
               return null;
            }

            if (g < e) {
               return entity22;
            }
         }

         return null;
      }
   }

   public double distanceTo(double x, double y, double z, double x1, double y1, double z1) {
      return Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z));
   }

   private static List<Float> getRotationValues(class_243 vec) {
      vec = vec.method_1020(ApeClient.mc.method_1560().method_5836(ApeClient.mc.method_1488()));
      List<Float> returnList = Lists.newArrayList();
      returnList.add(((float)Math.toDegrees(Math.atan2(vec.method_10215(), vec.method_10216())) - 90.0F) % 360.0F);
      returnList.add(
         class_3532.method_15393(
            (float)(
               -Math.toDegrees(Math.atan2(vec.method_10214(), Math.sqrt(vec.method_10216() * vec.method_10216() + vec.method_10215() * vec.method_10215())))
            )
         )
      );
      return returnList;
   }

   public double getClosestPointDistance(class_1297 ent) {
      class_1297 entity = ApeClient.mc.method_1560();
      class_243 localEyePosition = entity.method_5836(ApeClient.mc.method_1488());
      class_238 boundingBox = ent.method_5829().method_1014((double)ent.method_5871());
      double xPos = class_3532.method_15350(localEyePosition.method_10216(), boundingBox.field_1323, boundingBox.field_1320);
      double yPos = class_3532.method_15350(localEyePosition.method_10214(), boundingBox.field_1322, boundingBox.field_1325);
      double zPos = class_3532.method_15350(localEyePosition.method_10215(), boundingBox.field_1321, boundingBox.field_1324);
      return this.distanceTo(xPos, yPos, zPos, localEyePosition.method_10216(), localEyePosition.method_10214(), localEyePosition.method_10215());
   }

   public static List<Float> getRotation(class_1297 ent, double attackRange) {
      if (xTween.isFinished()) {
         xTween.setDestinationValue((new Random().nextDouble() - 0.5) * 100.0);
      }

      if (yTween.isFinished()) {
         yTween.setDestinationValue((new Random().nextDouble() - 0.5) * 100.0);
      }

      if (zTween.isFinished()) {
         zTween.setDestinationValue((new Random().nextDouble() - 0.5) * 100.0);
      }

      class_1297 entity = ApeClient.mc.method_1560();
      class_243 localEyePosition = entity.method_5836(ApeClient.mc.method_1488());
      class_238 boundingBox = ent.method_5829().method_1014((double)ent.method_5871());
      double clampedX = class_3532.method_15350(localEyePosition.method_10216(), boundingBox.field_1323, boundingBox.field_1320);
      double clampedY = class_3532.method_15350(localEyePosition.method_10214(), boundingBox.field_1322, boundingBox.field_1325);
      double clampedZ = class_3532.method_15350(localEyePosition.method_10215(), boundingBox.field_1321, boundingBox.field_1324);
      double xPos = class_3532.method_15350(ent.method_23317() + (double)xTween.getValue() / 200.0, boundingBox.field_1323, boundingBox.field_1320);
      double yPos = class_3532.method_15350(clampedY + (double)yTween.getValue() / 200.0, boundingBox.field_1322, boundingBox.field_1325);
      double zPos = class_3532.method_15350(ent.method_23321() + (double)zTween.getValue() / 200.0, boundingBox.field_1321, boundingBox.field_1324);
      double dist = new class_243(clampedX, clampedY, clampedZ).method_1022(localEyePosition);
      double lerpValue = class_3532.method_15350((dist - (attackRange - 0.5)) / 0.5, 0.0, 1.0);
      xPos = class_3532.method_16436(lerpValue, xPos, clampedX);
      yPos = class_3532.method_16436(lerpValue, yPos, clampedY);
      zPos = class_3532.method_16436(lerpValue, zPos, clampedZ);
      return getRotationValues(new class_243(xPos, yPos, zPos));
   }

   public class_1297 getEntity(double reach, double realReach) {
      class_1297 returnEnt = null;
      double hp = 0.0;
      if (ApeClient.mc.field_1724 != null && ApeClient.mc.field_1687 != null) {
         for (class_1657 ent : ApeClient.mc.field_1687.method_18456()) {
            if (ent != ApeClient.mc.field_1724 && PlayerUtil.isTargetable(ent, true, true, ClickGui.pane3.teams.enabled)) {
               double dist = this.getClosestPointDistance(ent);
               double realHp = (double)(ent.method_6032() + ent.method_6067());
               if (dist < reach && (realHp <= hp || hp == 0.0) && ent.method_5805()) {
                  List<Float> rots = getRotation(ent, (double)this.attackRange.value / 10.0);
                  float ang = Math.abs(class_3532.method_15393(rots.get(0) - class_3532.method_15393(ApeClient.mc.field_1724.method_36454())));
                  if (!(ang > (float)(this.maxAngle.value / 2))) {
                     boolean check = true;
                     if (this.wallCheck.enabled) {
                        check = ApeClient.mc.field_1724.method_6057(ent);
                     }

                     if (check) {
                        hp = realHp;
                        reach = dist;
                        returnEnt = ent;
                     }
                  }
               }
            }
         }
      }

      return returnEnt;
   }

   public float randomFloat() {
      return new Random().nextFloat() * (float)(new Random().nextInt(2) == 1 ? 1 : -1);
   }

   @EventListener
   public void cameraTick(CameraUpdateEvent event) {
      float timePassed = event.timePassed * 1000.0F;
      if ((this.isAttacking && this.lastTarget != null || this.returning) && ApeClient.mc.field_1724 != null) {
         List<Float> rots = this.returning
            ? Arrays.asList(ApeClient.mc.field_1724.method_36454(), ApeClient.mc.field_1724.method_36455())
            : getRotation(this.lastTarget, (double)this.attackRange.value / 10.0);
         float f = (float)((Double)ApeClient.mc.field_1690.method_42495().method_41753() * 0.6F + 0.2F);
         float f1 = f * f * f * 8.0F;
         float aimCalculated = (float)this.aimSpeed.value / 10.0F;
         float deltaYaw = class_3532.method_15393(rots.get(0) - class_3532.method_15393(this.auraYaw));
         float deltaPitch = class_3532.method_15393(rots.get(1) - class_3532.method_15393(this.auraPitch));
         int maxSpeedYaw = (int)((aimCalculated + this.randomFloat()) * timePassed / f1);
         int maxSpeedPitch = (int)((aimCalculated + this.randomFloat()) * timePassed / f1);
         maxSpeedYaw = (int)((float)maxSpeedYaw / (Math.abs(deltaPitch) > Math.abs(deltaYaw) ? Math.abs(deltaPitch) / Math.abs(deltaYaw) : 1.0F));
         maxSpeedPitch = (int)((float)maxSpeedPitch / (Math.abs(deltaYaw) > Math.abs(deltaPitch) ? Math.abs(deltaYaw) / Math.abs(deltaPitch) : 1.0F));
         float aimSpeedYaw = (float)class_3532.method_15340((int)((double)Math.round(deltaYaw / f1) / 0.15), -maxSpeedYaw, maxSpeedYaw) * f1;
         float aimSpeedPitch = (float)class_3532.method_15340((int)((double)Math.round(deltaPitch / f1) / 0.15), -maxSpeedPitch, maxSpeedPitch) * f1;
         this.auraYaw = (float)((double)this.auraYaw + (double)aimSpeedYaw * 0.15);
         this.auraPitch = class_3532.method_15363((float)((double)this.auraPitch + (double)aimSpeedPitch * 0.15), -90.0F, 90.0F);
         if (Math.abs(deltaYaw) < 2.0F && Math.abs(deltaPitch) < 2.0F && this.returning) {
            this.returning = false;
         }
      }
   }

   @EventListener
   public void playerMoveEvent(PlayerMoveEvent event) {
      if (this.isAttacking || this.returning) {
         event.yaw = this.auraYaw;
         event.pitch = this.auraPitch;
      }
   }

   @EventListener
   public void actionKeyTick(KeyPollingEvent event) {
      if (this.goodJump) {
         this.goodJump = !ApeClient.mc.field_1724.method_24828() && !ApeClient.mc.field_1724.method_5799();
      }

      class_1799 hand = ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_5998(class_1268.field_5808) : new class_1799(class_1802.field_8162);
      Collection<class_1322> damageAttribute = hand.method_7926(class_1304.field_6173).get(class_5134.field_23721);
      if (damageAttribute.size() > 0 && (!this.swordOnly.enabled || hand.method_7909() instanceof class_1829)) {
         double reach = (double)this.attackRange.value / 10.0;
         double swingReach = (double)this.swingRange.value / 10.0;
         this.lastTarget = this.getEntity(swingReach, reach);
         boolean newAttacking = this.lastTarget != null;
         if (this.isAttacking != newAttacking) {
            this.isAttacking = newAttacking;
            if (newAttacking && !this.returning) {
               this.auraYaw = ApeClient.mc.field_1724.method_36454();
               this.auraPitch = ApeClient.mc.field_1724.method_36455();
               this.cameraTick(new CameraUpdateEvent(0.0F, 0, 0));
            }

            this.returning = !newAttacking;
         }

         this.isAttacking = newAttacking;
         if (this.isAttacking) {
            float attackProgress = ApeClient.mc.field_1724.method_7261(0.0F);
            class_1309 ent = (class_1309)this.lastTarget;
            ClickGui.targetInfo.target = ent;
            ClickGui.targetInfo.targetTime = System.currentTimeMillis() + 1000L;
            if (ent.method_6032() > 0.0F && ent.method_5732() && this.getViewEntity(this.auraYaw, this.auraPitch) == this.lastTarget) {
               boolean doSwing = false;
               if (ent.method_6061(ApeClient.mc.field_1687.method_48963().method_48802(ApeClient.mc.field_1724)) && hand.method_7909() instanceof class_1743) {
                  doSwing = ApeClient.mc.field_1724.method_5624() && attackProgress > 0.1F;
               } else if (attackProgress > 0.9F) {
                  doSwing = true;
                  if (!ApeClient.mc.field_1724.method_5624()
                     && !ApeClient.mc.field_1724.method_24828()
                     && !ApeClient.mc.field_1724.method_6101()
                     && !ApeClient.mc.field_1724.method_5799()
                     && !ApeClient.mc.field_1724.method_6059(class_1294.field_5919)
                     && !ApeClient.mc.field_1724.method_6059(class_1294.field_5902)
                     && !ApeClient.mc.field_1724.method_5765()) {
                     doSwing = ApeClient.mc.field_1724.field_6017 > 0.0F;
                  }
               }

               if (doSwing && ((MinecraftClientAccessor)ApeClient.mc).getAttackCooldown() <= 0) {
                  ApeClient.mc.field_1761.method_2918(ApeClient.mc.field_1724, this.lastTarget);
                  ApeClient.mc.field_1724.method_6104(class_1268.field_5808);
               }
            }
         }
      } else {
         if (this.lastTarget != null) {
            this.isAttacking = false;
            this.returning = true;
         }

         this.lastTarget = null;
      }
   }

   @Override
   public String extra() {
      return class_124.field_1080 + " Silent";
   }
}
