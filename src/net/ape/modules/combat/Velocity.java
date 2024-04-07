package net.ape.modules.combat;

import java.util.Arrays;
import java.util.Random;
import net.ape.ApeClient;
import net.ape.events.EventListener;
import net.ape.events.GameTickEvent;
import net.ape.events.PacketEvent;
import net.ape.events.VelocityEvent;
import net.ape.gui.ClickGui;
import net.ape.gui.components.clickgui.Dropdown;
import net.ape.gui.components.clickgui.Slider;
import net.ape.gui.components.clickgui.Toggle;
import net.ape.modules.Module;
import net.minecraft.class_124;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_6374;
import org.lwjgl.glfw.GLFW;

public class Velocity extends Module {
   public Dropdown mode = new Dropdown("Mode", this, Arrays.asList("Packet", "Inverse", "Tick", "Clip", "Jump", "Edit", "Transaction"), value -> {
      this.tick.visible = value.equals("Tick") || value.equals("Inverse");
      this.horizontal.visible = !value.equals("Jump");
      this.vertical.visible = !value.equals("Jump");
   });
   public Slider horizontal = new Slider("Horizontal", this, 0, 100, 100, 1.0);
   public Slider vertical = new Slider("Vertical", this, 0, 100, 100, 1.0);
   public Slider chance = new Slider("Chance", this, 0, 100, 100, 1.0);
   public Slider tick = new Slider("Tick", this, 1, 10, 1, 1.0);
   public Toggle water = new Toggle("Water check", this, null);
   public Toggle explosion = new Toggle("Ignore Explosions", this, null);
   class_243 editPosition;
   class_243 velo;
   int vtick;
   boolean jump;
   int transactionCancel = 0;

   public Velocity() {
      super("Velocity", ClickGui.combat);
   }

   @EventListener
   public void velocityEvent(VelocityEvent event) {
      if (new Random().nextInt(100) <= this.chance.value) {
         if (!this.explosion.enabled || event.velocityType != VelocityEvent.VelocityType.EXPLOSION) {
            if (!this.water.enabled || !ApeClient.mc.field_1724.method_52535()) {
               float hori = (float)this.horizontal.value / 100.0F;
               float vert = (float)this.vertical.value / 100.0F;
               String var4 = this.mode.value;
               switch (var4) {
                  case "Clip":
                     if (ApeClient.mc.field_1724.field_36331) {
                        ApeClient.mc
                           .field_1724
                           .method_5814(
                              ApeClient.mc.field_1724.method_23317(),
                              ApeClient.mc.field_1724.method_23318() - event.y * (event.y > 0.0 ? Math.min(event.y / 0.0815999984741211, 5.0) : 0.0),
                              ApeClient.mc.field_1724.method_23321()
                           );
                     }
                     break;
                  case "Edit":
                     this.editPosition = new class_243(
                        ApeClient.mc.field_1724.method_23317() + event.x,
                        ApeClient.mc.field_1724.method_23318() + event.y,
                        ApeClient.mc.field_1724.method_23321() + event.z
                     );
                     event.cancel();
                     break;
                  case "Jump":
                     if (ApeClient.mc.field_1724.method_5624() && ApeClient.mc.field_1724.field_36331) {
                        this.velo = new class_243(0.0, 0.0, 0.0);
                        this.vtick = 2;
                        class_304.method_1416(ApeClient.mc.field_1690.field_1903.method_1429(), true);
                     }
                     break;
                  default:
                     class_243 velo = ApeClient.mc.field_1724.method_18798();
                     if (!this.mode.value.equals("Packet")) {
                        velo = new class_243(velo.field_1352, velo.field_1351, velo.field_1350);
                        this.vtick = this.tick.value;
                     }

                     this.transactionCancel = 3;
                     ApeClient.mc
                        .field_1724
                        .method_5750(
                           hori == 0.0F ? velo.field_1352 : event.x * (double)hori,
                           vert == 0.0F ? velo.field_1351 : event.y * (double)vert,
                           hori == 0.0F ? velo.field_1350 : event.z * (double)hori
                        );
                     event.cancel();
               }
            }
         }
      }
   }

   @EventListener
   public void packetEvent(PacketEvent event) {
      if (event.packet instanceof class_6374 && this.mode.value.equals("Transaction") && this.transactionCancel > 0) {
         event.cancel();
         this.transactionCancel--;
      }
   }

   @EventListener
   public void worldTick(GameTickEvent event) {
      if (ApeClient.mc.field_1724 != null && ApeClient.mc.field_1687 != null && this.vtick > 0) {
         this.vtick--;
         if (this.vtick == 0 && this.velo != null) {
            class_243 pvelo = ApeClient.mc.field_1724.method_18798();
            if (this.mode.value.equals("Inverse")) {
               ApeClient.mc.field_1724.method_5750(pvelo.field_1352 * -1.0, pvelo.field_1351, pvelo.field_1350 * -1.0);
            } else if (this.mode.value.equals("Tick")) {
               ApeClient.mc.field_1724.method_5750(0.0, pvelo.field_1351, 0.0);
            } else if (this.mode.value.equals("Jump")) {
               class_304.method_1416(
                  ApeClient.mc.field_1690.field_1903.method_1429(),
                  GLFW.glfwGetKey(ApeClient.mc.method_22683().method_4490(), ApeClient.mc.field_1690.field_1903.method_1429().method_1444()) == 1
               );
            }
         }
      }
   }

   @Override
   public String extra() {
      return class_124.field_1080 + " " + this.mode.value;
   }
}
