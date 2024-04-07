package net.ape.handlers;

import com.google.common.collect.Lists;
import java.util.List;
import net.ape.modules.Module;
import net.ape.modules.blatant.AutoBlock;
import net.ape.modules.blatant.Blink;
import net.ape.modules.blatant.Disabler;
import net.ape.modules.blatant.Fly;
import net.ape.modules.blatant.Gravity;
import net.ape.modules.blatant.HighJump;
import net.ape.modules.blatant.KeepSprint;
import net.ape.modules.blatant.Killaura;
import net.ape.modules.blatant.NoSlowdown;
import net.ape.modules.blatant.Speed;
import net.ape.modules.blatant.Timer;
import net.ape.modules.combat.Sprint;
import net.ape.modules.combat.TriggerBot;
import net.ape.modules.combat.Velocity;
import net.ape.modules.combat.WTap;
import net.ape.modules.render.Chams;
import net.ape.modules.render.ESP;
import net.ape.modules.render.Fullbright;
import net.ape.modules.render.Health;
import net.ape.modules.render.NameTags;
import net.ape.modules.world.CobwebBypass;
import net.ape.modules.world.MLG;

public class ModuleHandler {
   public static List<Module> modules = Lists.newArrayList();
   public static KeepSprint keepSprint = new KeepSprint();
   public static NoSlowdown noSlow = new NoSlowdown();
   public static Timer timer = new Timer();
   public static Gravity gravity = new Gravity();
   public static WTap wTap = new WTap();
   public static Sprint sprint = new Sprint();
   public static Chams chams = new Chams();
   public static Fullbright fullbright = new Fullbright();
   public static CobwebBypass cobwebBypass = new CobwebBypass();
   public static Disabler disabler = new Disabler();

   public static void init() {
      modules.add(new TriggerBot());
      modules.add(sprint);
      modules.add(keepSprint);
      modules.add(new Velocity());
      modules.add(noSlow);
      modules.add(new MLG());
      modules.add(new AutoBlock());
      modules.add(timer);
      modules.add(new Speed());
      modules.add(new Blink());
      modules.add(new NameTags());
      modules.add(gravity);
      modules.add(wTap);
      modules.add(chams);
      modules.add(new Health());
      modules.add(fullbright);
      modules.add(new Fly());
      modules.add(new Killaura());
      modules.add(new HighJump());
      modules.add(cobwebBypass);
      modules.add(new ESP());
      modules.add(disabler);
   }
}
