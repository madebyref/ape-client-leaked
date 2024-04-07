package net.ape.modules.render;

import java.util.concurrent.ConcurrentHashMap;
import net.ape.ApeClient;
import net.ape.events.EntityRenderEvent;
import net.ape.events.EventListener;
import net.ape.events.WorldChangeEvent;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.modules.Module;
import net.ape.utils.PlayerUtil;
import net.minecraft.class_1087;
import net.minecraft.class_124;
import net.minecraft.class_128;
import net.minecraft.class_129;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_148;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5250;
import net.minecraft.class_640;
import net.minecraft.class_811;
import net.minecraft.class_327.class_6415;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class NameTags extends Module {
   public final Toggle Health = new Toggle("Health", this, callback -> this.HealthBypass.visible = (Boolean)callback);
   public final Toggle HealthBypass = new Toggle("Health Bypass", this, null);
   public final Toggle Distance = new Toggle("Distance", this, null);
   public final Toggle Background = new Toggle("Background", this, null);
   public final Toggle Equipment = new Toggle("Equipment", this, null);
   ConcurrentHashMap<class_1297, class_5250> cached = new ConcurrentHashMap<>();

   public NameTags() {
      super("NameTags", ClickGui.render);
   }

   @EventListener
   public void rejoin(WorldChangeEvent event) {
      this.cached.clear();
   }

   @EventListener
   public void renderEntity(EntityRenderEvent event) {
      if (event.entity instanceof class_1657) {
         event.cancel();
         this.renderLabelIfPresent(event.entity, event.entity.method_5476(), event.matrices, event.vertexConsumerProvider, event.light);
      }
   }

   public static int[] getScreenCoords(double x, double y, double z, Matrix4f vec) {
      Vector3f screenCoords = new Vector3f();
      int[] viewport = new int[4];
      vec.project((float)x, (float)y, (float)z, viewport, screenCoords);
      return new int[]{(int)screenCoords.x, (int)(screenCoords.y - (float)ApeClient.mc.method_22683().method_4506())};
   }

   private void drawItem(
      class_4587 matrices, class_4597 vertexConsumer, @Nullable class_1309 entity, @Nullable class_1937 world, class_1799 stack, int x, int y, int seed, int z
   ) {
      if (!stack.method_7960()) {
         class_1087 bakedModel = ApeClient.mc.method_1480().method_4019(stack, world, entity, seed);
         matrices.method_22903();
         matrices.method_46416((float)(x + 8), (float)(y + 8), 0.0F);

         try {
            matrices.method_34425(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
            matrices.method_22905(16.0F, 16.0F, 16.0F);
            boolean var10000 = !bakedModel.method_24304();
            ApeClient.mc.method_1480().method_23179(stack, class_811.field_4315, false, matrices, vertexConsumer, 15728895, class_4608.field_21444, bakedModel);
         } catch (Throwable var14) {
            class_128 crashReport = class_128.method_560(var14, "Rendering item");
            class_129 crashReportSection = crashReport.method_562("Item being rendered");
            crashReportSection.method_577("Item Type", () -> String.valueOf(stack.method_7909()));
            crashReportSection.method_577("Item Damage", () -> String.valueOf(stack.method_7919()));
            crashReportSection.method_577("Item NBT", () -> String.valueOf(stack.method_7969()));
            crashReportSection.method_577("Item Foil", () -> String.valueOf(stack.method_7958()));
            throw new class_148(crashReport);
         }

         matrices.method_22909();
      }
   }

   private class_2561 getName(class_1657 entity) {
      if (ApeClient.mc.method_1562().method_2871(entity.method_7334().getId()) == null) {
         return class_2561.method_30163("");
      } else {
         for (class_640 ent : ApeClient.mc.method_1562().method_45732()) {
            class_2561 name = ApeClient.mc.field_1705.method_1750().method_1918(ent);
            if (name != null && name.getString().contains(entity.method_7334().getName()) && !ent.method_2966().equals(entity.method_7334())) {
               return name;
            }
         }

         return ApeClient.mc.field_1705.method_1750().method_1918(ApeClient.mc.method_1562().method_2871(entity.method_7334().getId()));
      }
   }

   protected void renderLabelIfPresent(class_1297 entity, class_2561 text, class_4587 matrices, class_4597 vertexConsumers, int light) {
      if (ApeClient.mc.field_1724 == null || entity.method_5628() != ApeClient.mc.field_1724.method_5628()) {
         if (PlayerUtil.isTargetable(entity, false, false, true)) {
            double d = ApeClient.mc.method_1561().method_23168(entity);
            class_1309 ent = (class_1309)entity;
            class_5250 str = this.cached.get(entity);
            if (str == null) {
               str = entity.method_5477().method_27661();
               if (entity instanceof class_1657) {
                  class_2561 newText = this.getName((class_1657)entity);
                  if (newText != null) {
                     str = newText.method_27661();
                  }
               }

               this.cached.put(entity, str.method_27661());
            } else {
               str = str.method_27661();
            }

            if (this.Health.enabled) {
               double health = PlayerUtil.getHealth(ent, this.HealthBypass.enabled);
               double absorption = Math.floor(Math.max(health - (double)ent.method_6063(), 0.0) * 10.0) / 10.0;
               health = Math.min(health, (double)ent.method_6063());
               double healthPercent = health / (double)ent.method_6063();
               class_124 textColor = class_124.field_1077;
               if (healthPercent <= 0.75) {
                  textColor = class_124.field_1054;
               }

               if (healthPercent <= 0.5) {
                  textColor = class_124.field_1065;
               }

               if (healthPercent <= 0.25) {
                  textColor = class_124.field_1079;
               }

               str = str.method_27693(" " + textColor + Math.floor(health / 2.0 * 10.0) / 10.0);
               if (absorption != 0.0) {
                  str = str.method_27693(" " + class_124.field_1065 + absorption);
               }
            }

            if (this.Distance.enabled && ApeClient.mc.field_1724 != null) {
               str = class_2561.method_30163(
                     class_124.field_1060
                        + "["
                        + class_124.field_1068
                        + Math.round(ApeClient.mc.field_1724.method_5739(ent))
                        + class_124.field_1060
                        + "] "
                        + class_124.field_1070
                  )
                  .method_27661()
                  .method_10852(str);
            }

            boolean bl = true;
            float f = entity.method_51152();
            int i = "deadmau5".equals(str.getString()) ? -10 : 0;
            matrices.method_22903();
            float f3 = 0.0025F * Math.max((float)Math.sqrt(d), 10.0F);
            matrices.method_46416(0.0F, f + (f3 - 0.025F) * 10.0F, 0.0F);
            matrices.method_22907(ApeClient.mc.method_1561().method_24197());
            matrices.method_22905(-f3, -f3, f3);
            Matrix4f matrix4f = matrices.method_23760().method_23761();
            float g = ApeClient.mc.field_1690.method_19343(this.Background.enabled ? 0.5F : 0.0F);
            int j = (int)(g * 255.0F) << 24;
            class_327 textRenderer = ApeClient.mc.field_1772;
            float h = (float)(-textRenderer.method_27525(str) / 2);
            textRenderer.method_30882(str, h, (float)i, -1, false, matrix4f, vertexConsumers, bl ? class_6415.field_33994 : class_6415.field_33993, j, light);
            if (bl) {
               textRenderer.method_30882(str, h, (float)i, -1, false, matrix4f, vertexConsumers, class_6415.field_33993, 0, light);
            }

            if (this.Equipment.enabled) {
               int i2 = 0;

               for (class_1799 item : ent.method_5661()) {
                  i2++;
                  this.drawItem(matrices, vertexConsumers, ApeClient.mc.field_1724, ApeClient.mc.field_1687, item, (int)h + 64 - 16 * i2, -18, 0, 0);
               }
            }

            matrices.method_22909();
         }
      }
   }
}
