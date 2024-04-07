package net.ape.modules.combat;

import java.util.Collection;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.JumpEvent;
import net.ape.events.KeyPollingEvent;
import net.ape.events.VelocityEvent;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.mixins.MinecraftClientAccessor;
import net.ape.modules.Module;
import net.ape.utils.PlayerUtil;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1322;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_304;
import net.minecraft.class_3966;
import net.minecraft.class_5134;
import net.minecraft.class_239.class_240;

public class TriggerBot extends Module {
   private final Toggle ignoreInvis = new Toggle("Ignore Invisibles", this, null);
   private final Toggle mobs = new Toggle("Mobs", this, null);
   private boolean goodJump = false;
   int swingTicks = 0;
   long velocityTick = System.currentTimeMillis();
   long waterTick = System.currentTimeMillis();

   public TriggerBot() {
      super("TriggerBot", ClickGui.combat);
   }

   private boolean canAttack() {
      for (class_1657 ent : ApeClient.mc.field_1687.method_18456()) {
         if (ent != ApeClient.mc.field_1724 && ApeClient.mc.field_1724.method_5739(ent) < 20.0F) {
            return false;
         }
      }

      return true;
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @EventListener
   public void jumpTick(JumpEvent event) {
      this.goodJump = true;
   }

   @EventListener
   public void velocityEvent(VelocityEvent event) {
      this.velocityTick = System.currentTimeMillis() + 500L;
   }

   @EventListener
   public void actionTick(KeyPollingEvent event) {
      this.waterTick = ApeClient.mc.field_1724.method_5869() ? System.currentTimeMillis() + 1000L : this.waterTick;
      if (ApeClient.mc.field_1765 != null && ApeClient.mc.field_1765.method_17783() == class_240.field_1331) {
         class_1799 hand = ApeClient.mc.field_1724 != null ? ApeClient.mc.field_1724.method_5998(class_1268.field_5808) : new class_1799(class_1802.field_8162);
         Collection<class_1322> damageAttribute = hand.method_7926(class_1304.field_6173).get(class_5134.field_23721);
         if (damageAttribute.size() > 0
            && ((MinecraftClientAccessor)ApeClient.mc).getAttackCooldown() <= 0
            && !ApeClient.mc.field_1690.field_1886.method_1434()
            && ApeClient.mc.field_1692 instanceof class_1309) {
            if (this.mobs.enabled) {
               if (!(ApeClient.mc.field_1692 instanceof class_1657) && !this.canAttack()) {
                  return;
               }
            } else if (!(ApeClient.mc.field_1692 instanceof class_1657)) {
               return;
            }

            class_1309 ent = (class_1309)((class_3966)ApeClient.mc.field_1765).method_17782();
            if (this.ignoreInvis.enabled && ent.method_5767()) {
               return;
            }

            if (PlayerUtil.isTargetable(ent, true, true, ClickGui.pane3.teams.enabled)) {
               ClickGui.targetInfo.target = ent;
               ClickGui.targetInfo.targetTime = System.currentTimeMillis() + 1000L;
               float attackProgress = ApeClient.mc.field_1724.method_7261(0.0F);
               if (ent.method_6032() > 0.0F && ent.method_5732()) {
                  boolean doSwing = false;
                  if (ent.method_6061(ApeClient.mc.field_1687.method_48963().method_48802(ApeClient.mc.field_1724)) && hand.method_7909() instanceof class_1743
                     )
                   {
                     doSwing = ApeClient.mc.field_1724.method_5624() && attackProgress > 0.1F;
                  } else if (attackProgress > 0.9F) {
                     doSwing = true;
                     this.swingTicks++;
                     if (this.waterTick < System.currentTimeMillis()
                        && this.velocityTick < System.currentTimeMillis()
                        && this.swingTicks < 4
                        && !ApeClient.mc.field_1724.method_5624()
                        && !ApeClient.mc.field_1724.method_24828()
                        && !ApeClient.mc.field_1724.method_6101()
                        && !ApeClient.mc.field_1724.method_5799()
                        && !ApeClient.mc.field_1724.method_6059(class_1294.field_5919)
                        && !ApeClient.mc.field_1724.method_6059(class_1294.field_5902)
                        && !ApeClient.mc.field_1724.method_5765()) {
                        doSwing = ApeClient.mc.field_1724.field_6017 > 0.0F;
                     }
                  }

                  if (doSwing) {
                     class_304.method_1420(ApeClient.mc.field_1690.field_1886.method_1429());
                     this.swingTicks = 0;
                  }
               }
            }
         }
      }
   }
}
