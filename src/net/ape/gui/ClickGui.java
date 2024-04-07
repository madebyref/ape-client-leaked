package net.ape.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.ListIterator;
import net.ape.ApeClient;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.clickgui.Button;
import net.ape.gui.components.clickgui.Divider;
import net.ape.gui.components.clickgui.MainWindow;
import net.ape.gui.components.clickgui.OverlayBar;
import net.ape.gui.components.clickgui.OverlayWindow;
import net.ape.gui.components.clickgui.SettingsPane;
import net.ape.gui.components.clickgui.Window;
import net.ape.gui.components.clickgui.overlays.TargetInfo;
import net.ape.gui.components.clickgui.overlays.TextGUI;
import net.ape.gui.components.clickgui.settings.GuiPane;
import net.ape.gui.components.clickgui.settings.MainPane;
import net.ape.gui.components.clickgui.settings.ModulesPane;
import net.ape.gui.components.clickgui.settings.NotificationsPane;
import net.ape.modules.Module;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_8251;
import org.joml.Matrix4f;

public class ClickGui extends class_437 {
   public List<GuiObject> objects = Lists.newArrayList();
   public static MainWindow main = new MainWindow();
   public static Window combat = new Window("Combat", new class_2960("ape/combattab.png"), 15, 16);
   public static Window blatant = new Window("Blatant", new class_2960("ape/blatanttab.png"), 16, 16);
   public static Window render = new Window("Render", new class_2960("ape/rendertab.png"), 17, 16);
   public static Window utility = new Window("Utility", new class_2960("ape/utilitytab.png"), 17, 16);
   public static Window world = new Window("World", new class_2960("ape/worldtab.png"), 16, 16);
   public static Window inventory = new Window("Inventory", new class_2960("ape/inventorytab.png"), 17, 16);
   public static Window minigames = new Window("Minigames", new class_2960("ape/minitab.png"), 22, 14);
   public static OverlayWindow textgui = new TextGUI();
   public static TargetInfo targetInfo = new TargetInfo();
   public static OverlayBar bar = new OverlayBar();
   public static MainPane pane = new MainPane();
   public static SettingsPane pane2 = new SettingsPane("General", pane);
   public static ModulesPane pane3 = new ModulesPane(pane);
   public static GuiPane pane4 = new GuiPane(pane);
   public static NotificationsPane pane5 = new NotificationsPane(pane);
   public static Module binding;

   public ClickGui() {
      super(class_2561.method_43473());
      main.objects.add(new Divider());
      main.objects.add(new Button("Combat", new class_2960("ape/combaticon.png"), 13, 14, combat));
      main.objects.add(new Button("Blatant", new class_2960("ape/blatanticon.png"), 14, 14, blatant));
      main.objects.add(new Button("Render", new class_2960("ape/rendericon.png"), 15, 14, render));
      main.objects.add(new Button("Utility", new class_2960("ape/utilityicon.png"), 15, 14, utility));
      main.objects.add(new Button("World", new class_2960("ape/worldicon.png"), 14, 14, world));
      main.objects.add(new Button("Inventory", new class_2960("ape/inventoryicon.png"), 15, 14, inventory));
      main.objects.add(new Button("Minigames", new class_2960("ape/miniicon.png"), 19, 12, minigames));
      main.objects.add(new Divider("MISC"));
      main.objects.add(new Button("Friends", null));
      main.objects.add(new Button("Profiles", null));
      main.objects.add(new Button("Targets", null));
      main.objects.add(new Divider());
      main.objects.add(bar);
      main.objects.add(pane);
      main.objects.add(pane2);
      main.objects.add(pane3);
      main.objects.add(pane4);
      main.objects.add(pane5);
      this.objects.add(main);
      this.objects.add(combat);
      this.objects.add(blatant);
      this.objects.add(render);
      this.objects.add(utility);
      this.objects.add(world);
      this.objects.add(inventory);
      this.objects.add(minigames);
      this.objects.add(textgui);
      this.objects.add(targetInfo);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      mouseX = (int)((double)mouseX * this.field_22787.method_22683().method_4495());
      mouseY = (int)((double)mouseY * this.field_22787.method_22683().method_4495());
      RenderSystem.setProjectionMatrix(
         new Matrix4f()
            .setOrtho(0.0F, (float)this.field_22787.method_22683().method_4489(), (float)this.field_22787.method_22683().method_4506(), 0.0F, 1000.0F, 21000.0F),
         class_8251.field_43361
      );
      int i = 0;

      for (GuiObject object : this.objects) {
         object.render(mouseX, mouseY, context, object.x, object.y, i);
         i++;
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setProjectionMatrix(
         new Matrix4f()
            .setOrtho(
               0.0F,
               (float)((double)this.field_22787.method_22683().method_4489() / this.field_22787.method_22683().method_4495()),
               (float)((double)this.field_22787.method_22683().method_4506() / this.field_22787.method_22683().method_4495()),
               0.0F,
               1000.0F,
               21000.0F
            ),
         class_8251.field_43361
      );
   }

   public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      mouseX *= this.field_22787.method_22683().method_4495();
      mouseY *= this.field_22787.method_22683().method_4495();
      ListIterator<GuiObject> reversed = this.objects.listIterator(this.objects.size());

      while (reversed.hasPrevious()) {
         GuiObject obj = reversed.previous();
         if (obj.visible && obj.mouseClicked(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      mouseX *= this.field_22787.method_22683().method_4495();
      mouseY *= this.field_22787.method_22683().method_4495();
      ListIterator<GuiObject> reversed = this.objects.listIterator(this.objects.size());

      while (reversed.hasPrevious()) {
         GuiObject obj = reversed.previous();
         if (obj.mouseReleased(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (binding != null) {
         binding.key = binding.key == keyCode ? -99 : keyCode;
         binding = null;
         return true;
      } else if (keyCode == ApeClient.keybind) {
         this.method_25419();
         return true;
      } else {
         for (GuiObject obj : this.objects) {
            obj.keyTyped(keyCode);
         }

         return super.method_25404(keyCode, scanCode, modifiers);
      }
   }

   public void onChar(char key) {
      for (GuiObject obj : this.objects) {
         obj.charTyped(key);
      }
   }

   public boolean method_25421() {
      return false;
   }
}
