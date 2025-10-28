package shodrone.customerapp.presentation;

import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import shodrone.core.infrastructure.Application;
import shodrone.customerapp.CustomerAppCli;

public class MainMenu extends AbstractUI {

	private static final int MY_PROFILE_OPTION = 1;
	private static final int NOTIFICATIONS_OPTION = 2;
	private static final int MY_SHOWS_OPTION = 3;
	private static final int EXIT_OPTION = 0;

	private static final String SEPARATOR_LABEL = "--------------";

	@Override
	public boolean show() {
		drawFormTitle();
		return doShow();
	}

	@Override
	protected boolean doShow() {
		final var menu = build();
		final MenuRenderer renderer;
		if (Application.settings().isMenuLayoutHorizontal()) {
			renderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
		} else {
			renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
		}
		return renderer.render();
	}

	public static Menu build() {
		final var mainMenu = new Menu();

		final Menu myProfileMenu = new MyProfileMenu();
		mainMenu.addSubMenu(MY_PROFILE_OPTION, myProfileMenu);

		if (!Application.settings().isMenuLayoutHorizontal()) {
			mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
		}

		CustomerAppCli.getInstance();
		int notificationsCount = CustomerAppCli.getNotificationsCount();
		final Menu notificationsMenu = new NotificationsMenu(notificationsCount);
		mainMenu.addSubMenu(NOTIFICATIONS_OPTION, notificationsMenu);

		if (!Application.settings().isMenuLayoutHorizontal()) {
			mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
		}

		final Menu myShowsMenu = new MyShowsMenu();
		mainMenu.addSubMenu(MY_SHOWS_OPTION, myShowsMenu);

		mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

		return mainMenu;
	}

	@Override
	public String headline() {
		return "Shodrone Customer App"; 
	}

}
