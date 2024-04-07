package net.ape.gui.components;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.class_332;
import org.json.JSONObject;

public class GuiObject {
   public List<GuiObject> objects = Lists.newArrayList();
   public boolean visible = true;
   public int x;
   public int y;
   int lastX;
   int lastY;

   public boolean render(int mouseX, int mouseY, class_332 context, int x, int y, int ind) {
      if (!this.visible) {
         return true;
      } else {
         this.x = x;
         this.y = y;
         this.lastX = x;
         this.lastY = y;
         return false;
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!this.visible) {
         return false;
      } else {
         for (GuiObject obj : this.objects) {
            if (obj.mouseClicked(mouseX, mouseY, button)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      for (GuiObject obj : this.objects) {
         if (obj.mouseReleased(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   public void keyTyped(int keyCode) {
      for (GuiObject obj : this.objects) {
         obj.keyTyped(keyCode);
      }
   }

   public void charTyped(char key) {
      for (GuiObject obj : this.objects) {
         obj.charTyped(key);
      }
   }

   public int getSize() {
      return 0;
   }

   public void save(JSONObject saved) {
      for (GuiObject obj : this.objects) {
         obj.save(saved);
      }
   }

   public void load(JSONObject saved) {
      for (GuiObject obj : this.objects) {
         obj.load(saved);
      }
   }
}
