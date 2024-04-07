package net.ape.gui.components.clickgui.settings;

import net.ape.gui.components.clickgui.SettingsPane;
import net.ape.gui.components.clickgui.Toggle;

public class ModulesPane extends SettingsPane {
   public Toggle teams = new Toggle("Teams by server", this, null, true);

   public ModulesPane(SettingsPane pane) {
      super("Modules", pane);
   }
}
