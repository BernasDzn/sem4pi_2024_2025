package shodrone.customerapp.presentation;

import eapli.framework.actions.menu.Menu;

public class MyShowsMenu extends Menu {

    private static final String MENU_TITLE = "My Shows >";

    private static final int EXIT_OPTION = 0;

    // MY SHOWS
    private static final int ALL_SHOWS_OPTION = 1;
    private static final int SCHEDULED_SHOWS_OPTION = 2;

    public MyShowsMenu() {
        super(MENU_TITLE);
        buildMyShowsMenu();
    }

    private void buildMyShowsMenu() {
        addSubMenu(ALL_SHOWS_OPTION, new AllShowsMenu());
        addSubMenu(SCHEDULED_SHOWS_OPTION, new ScheduledShowsMenu());
        addItem(EXIT_OPTION, "Return ", () -> true);
    }

}
