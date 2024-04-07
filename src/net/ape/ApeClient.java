package net.ape;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.ape.events.RenderHudEvent;
import net.ape.gui.ClickGui;
import net.ape.gui.components.GuiObject;
import net.ape.gui.components.Notification;
import net.ape.gui.components.clickgui.ColorSlider;
import net.ape.gui.components.clickgui.OverlayWindow;
import net.ape.gui.components.clickgui.settings.GUIColorSlider;
import net.ape.gui.font.CustomFont;
import net.ape.gui.font.fontrenderer.TTFFontRenderer;
import net.ape.handlers.EventHandler;
import net.ape.handlers.ModuleHandler;
import net.ape.handlers.NotificationHandler;
import net.ape.modules.Module;
import net.ape.utils.RenderUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_8251;
import org.joml.Matrix4f;
import org.json.JSONObject;

@Environment(EnvType.CLIENT)
public class ApeClient implements ClientModInitializer {
   public static class_310 mc = class_310.method_1551();
   public static CustomFont font;
   public static ClickGui gui;
   public static int keybind = 344;
   public static String realpath = mc.method_1479().toString().replace("resourcepacks", "ape");
   public static String currentConfig = "default";
   static ConcurrentLinkedQueue<String> saveQueue = new ConcurrentLinkedQueue<>();
   public static ConcurrentLinkedQueue<GuiObject> rainbowQueue = new ConcurrentLinkedQueue<>();
   static boolean loaded;
   static class_2960 notif = new class_2960("ape/notification.png");
   static TTFFontRenderer render;
   public static ConcurrentLinkedQueue<Notification> notifis = new ConcurrentLinkedQueue<>();

   public static void init() {
      font = new CustomFont();
      gui = new ClickGui();
      render = CustomFont.FONT_MANAGER.getFont("Arial 14");
      ModuleHandler.init();
      EventHandler.init();
      File path = new File(realpath);
      if (!path.exists()) {
         path.mkdir();
      }

      load(currentConfig);
   }

   public static void render(class_332 context) {
      int newRainbowValue = (int)(
         (1.0F + (float)(System.currentTimeMillis() / (long)Math.max((int)(5.0F / ((float)ClickGui.pane4.rainbowSpeed.value / 10.0F)), 1) % 1000L) / 1000.0F)
            % 1.0F
            * 100.0F
      );

      for (GuiObject obj : rainbowQueue) {
         if (obj instanceof ColorSlider object) {
            object.value = newRainbowValue;
            object.positionTween.setDestinationValue((double)class_3532.method_15363(200.0F * ((float)object.value / 100.0F), 0.0F, 185.0F));
         }
      }

      RenderSystem.setProjectionMatrix(
         new Matrix4f().setOrtho(0.0F, (float)mc.method_22683().method_4489(), (float)mc.method_22683().method_4506(), 0.0F, 1000.0F, 21000.0F),
         class_8251.field_43361
      );
      if (mc.field_1755 == null || !mc.field_1755.equals(gui)) {
         for (GuiObject objx : gui.objects) {
            if (objx instanceof OverlayWindow && ((OverlayWindow)objx).pinned && objx.visible) {
               ((OverlayWindow)objx).customRender(0, context);
            }
         }
      }

      int i = 0;

      for (Notification not : notifis) {
         renderNotification(not, ++i, context);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      EventHandler.fireEvent(new RenderHudEvent(context));
      RenderSystem.setProjectionMatrix(
         new Matrix4f()
            .setOrtho(
               0.0F,
               (float)((double)mc.method_22683().method_4489() / mc.method_22683().method_4495()),
               (float)((double)mc.method_22683().method_4506() / mc.method_22683().method_4495()),
               0.0F,
               1000.0F,
               21000.0F
            ),
         class_8251.field_43361
      );
   }

   public static void renderNotification(Notification not, int order, class_332 context) {
      if (not.time - System.currentTimeMillis() <= 200L && not.positionTween.isFinished()) {
         not.positionTween.setDestinationValue(0.0);
      }

      int posX = mc.method_22683().method_4489() - not.positionTween.getValue();
      int posY = mc.method_22683().method_4506() - not.positionYTween.getValue();
      not.positionYTween.setDestinationValueFirst((double)(37 + 80 * order));
      RenderUtil.image(context, notif, posX, posY, 0, 0, 40, 94, 40, 94, 276, 94);
      RenderUtil.image(context, notif, posX + 40, posY, 40, 0, 236, 94, not.sizeX - 40, 94, 276, 94);
      RenderUtil.image(context, not.icon, posX + 5, posY + 1, 60, 60, new Color(0, 0, 0, 128));
      RenderUtil.image(context, not.icon, posX + 4, posY, 60, 60);
      RenderUtil.rect(context, posX + 13, posY + 80, not.barTween.getValue(), 2, not.barColor);
      render.drawString(context, not.name, (float)(posX + 55), (float)(posY + 26), new Color(255, 255, 255).getRGB(), false);
      render.drawString(context, not.text, (float)(posX + 55), (float)(posY + 54), new Color(170, 170, 170).getRGB(), true);
      if (not.time < System.currentTimeMillis()) {
         notifis.remove(not);
      }
   }

   private static JSONObject readjson(String text) {
      try {
         return new JSONObject(text);
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   private static String readfile(String pathfile) {
      if (saveQueue.contains(pathfile)) {
         return "";
      } else {
         File file = new File(pathfile);

         try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
               String st;
               if ((st = br.readLine()) != null) {
                  return st;
               }
            }

            return "";
         } catch (IOException var7) {
            var7.printStackTrace();
            return "";
         }
      }
   }

   private static boolean isfile(String filepath) {
      return new File(filepath).exists();
   }

   public static void writefile(String filepath, String text) {
      if (!saveQueue.contains(filepath)) {
         try {
            saveQueue.add(filepath);
            File myObj = new File(filepath);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(filepath);
            myWriter.write(text);
            myWriter.close();
            saveQueue.remove(filepath);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }
   }

   public static void save(String fileName) {
      if (loaded) {
         fileName = "/apeprofile-" + fileName + ".json";
         JSONObject saved = new JSONObject();
         JSONObject saved2 = new JSONObject();
         saved.put("Modules", new JSONObject());
         saved.put("Legit", new JSONObject());
         saved2.put("Categories", new JSONObject());

         for (GuiObject obj : gui.objects) {
            obj.save(saved2.getJSONObject("Categories"));
         }

         for (Module obj : ModuleHandler.modules) {
            obj.button.save(saved.getJSONObject("Modules"));
         }

         writefile(realpath + fileName, saved.toString());
         writefile(realpath + "/ape-gui.json", saved2.toString());
      }
   }

   public static void load(String fileName) {
      boolean newLoaded = true;
      fileName = "/apeprofile-" + fileName + ".json";

      try {
         if (isfile(realpath + fileName)) {
            JSONObject data = readjson(readfile(realpath + fileName));
            if (data != null) {
               for (Module obj : ModuleHandler.modules) {
                  obj.button.load(data.getJSONObject("Modules"));
               }
            }
         }
      } catch (Exception var7) {
         NotificationHandler.addNotification("Vape", "Failed to load " + fileName + " profile.", "alert", 30.0);
         var7.printStackTrace();
         newLoaded = false;
      }

      try {
         if (isfile(realpath + "/ape-gui.json")) {
            JSONObject data = readjson(readfile(realpath + "/ape-gui.json"));
            if (data != null) {
               for (GuiObject obj : gui.objects) {
                  obj.load(data.getJSONObject("Categories"));
               }
            }
         }
      } catch (Exception var6) {
         NotificationHandler.addNotification("Vape", "Failed to load GUI settings.", "alert", 30.0);
         var6.printStackTrace();
         newLoaded = false;
      }

      loaded = newLoaded;
   }

   public static Color getRainbowColor(float h) {
      h = (
            1.0F
               + (float)(System.currentTimeMillis() / (long)Math.max((int)(5.0F / ((float)ClickGui.pane4.rainbowSpeed.value / 10.0F)), 1) % 1000L) / 1000.0F
               + h
         )
         % 1.0F;
      float s = 0.75F + 0.15F * Math.min(h / 0.03F, 1.0F);
      if (h > 0.57F) {
         s = 0.9F - 0.4F * Math.min((h - 0.57F) / 0.09F, 1.0F);
      }

      if (h > 0.66F) {
         s = 0.5F + 0.4F * Math.min((h - 0.66F) / 0.16F, 1.0F);
      }

      if (h > 0.87F) {
         s = 0.9F - 0.15F * Math.min((h - 0.87F) / 0.13F, 1.0F);
      }

      return Color.getHSBColor(h, s, 1.0F);
   }

   public static Color getRenderColor(float ind) {
      if (ClickGui.pane == null) {
         return Color.black;
      } else {
         GUIColorSlider slider = ClickGui.pane.guiTheme;
         return slider.getRainbow()
            ? getRainbowColor(ind)
            : Color.getHSBColor((float)slider.hue.value / 100.0F, (float)slider.sat.value / 100.0F, (float)slider.val.value / 100.0F);
      }
   }

   public static Color getRenderColor() {
      return getRenderColor(0.0F);
   }

   public static Color textColor() {
      if (ClickGui.pane.guiTheme.getRainbow()) {
         return Color.black;
      } else {
         Color guiColor = getRenderColor();
         float[] hsv = Color.RGBtoHSB(guiColor.getRed(), guiColor.getGreen(), guiColor.getBlue(), null);
         if ((double)hsv[2] < 0.7) {
            return Color.white;
         } else if ((double)hsv[1] < 0.6) {
            return Color.black;
         } else {
            return (double)hsv[0] > 0.04 && (double)hsv[0] < 0.56 ? Color.black : Color.white;
         }
      }
   }

   public void onInitializeClient() {
   }
}
