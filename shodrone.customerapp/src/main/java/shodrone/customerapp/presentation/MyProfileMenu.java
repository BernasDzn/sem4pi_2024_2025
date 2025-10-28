package shodrone.customerapp.presentation;

import eapli.framework.actions.menu.Menu;

public class MyProfileMenu extends Menu {

	private static final String MENU_TITLE = "My Profile >";

	private static final int EXIT_OPTION = 0;

	public MyProfileMenu() {
		super(MENU_TITLE);
		buildMyProfileMenu();
	}

	private void buildMyProfileMenu() {
		addItem(EXIT_OPTION, "Return ", () -> true);
	}



}
