package shodrone.backoffice.presentation;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.settings.ChangeMenuLayoutAction;

public class AdminMenu implements MenuOptionsAndLabels {

    // SETTINGS
    private static final int CHANGE_MENU_LAYOUT_OPTION = 1;

    public static Menu build() {
        final var menu = new Menu("Settings >");

        menu.addItem(CHANGE_MENU_LAYOUT_OPTION, "Change menu layout",
                new ChangeMenuLayoutAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    public String headline() {
        return "";
    }
}
