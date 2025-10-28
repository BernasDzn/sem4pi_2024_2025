/*
 * Copyright (c) 2013-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package shodrone.backoffice.presentation;

import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import shodrone.backoffice.presentation.customer.menu.CustomersMenu;
import shodrone.backoffice.presentation.customer_rep.menu.RepresentativesMenu;
import shodrone.backoffice.presentation.figure.menu.FigureMenu;
import shodrone.backoffice.presentation.figurecategory.menu.FigureCategoryMenu;
import shodrone.backoffice.presentation.showproposal.menu.ShowProposalMenu;
import shodrone.backoffice.presentation.showrequest.menu.ShowRequestMenu;
import shodrone.backoffice.presentation.user.menu.UserMenu;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.infrastructure.Application;
import shodrone.core.presentation.console.auth.MyUserMenu;

/**
 * TODO split this class in more specialized classes for each menu
 *
 * @author Paulo Gandra Sousa
 */
public class MainMenu extends AbstractUI implements MenuOptionsAndLabels {

	// MAIN MENU
	private static final int MY_USER_OPTION = 1;
	private static final int USERS_OPTION = 2;
	private static final int CUSTOMERS_OPTION = 3;
	private static final int CUSTOMER_REPRESENTATIVE_OPTION = 4;
	private static final int SHOW_REQUEST_OPTION = 5;
	private static final int FIGURE_OPTION = 6;
	private static final int FIGURE_CATEGORY_OPTION = 7;
	private static final int SHOW_PROPOSAL_OPTION = 8;
	private static final int SETTINGS_OPTION = 9;


	private static final String SEPARATOR_LABEL = "--------------";

	private static final AuthorizationService authz = AuthzRegistry.authorizationService();

	@Override
	public boolean show() {
		drawFormTitle();
		return doShow();
	}

	/**
	 * @return true if the user selected the exit option
	 */
	@Override
	public boolean doShow() {
		final var menu = build();
		final MenuRenderer renderer;
		if (Application.settings().isMenuLayoutHorizontal()) {
			renderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
		} else {
			renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
		}
		return renderer.render();
	}

	@Override
	public String headline() {

		return authz.session().map(s -> "Shodrone Backoffice [ @" + s.authenticatedUser().identity() + " ]")
				.orElse("Shodrone Backoffice [ ==Anonymous== ]");
	}

	public static Menu build() {
		final var mainMenu = new Menu();

		final Menu myUserMenu = new MyUserMenu();
		mainMenu.addSubMenu(MY_USER_OPTION, myUserMenu);

		if (!Application.settings().isMenuLayoutHorizontal()) {
			mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
			final var usersMenu = UserMenu.build();
			mainMenu.addSubMenu(USERS_OPTION, usersMenu);
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR,
				ShodroneRoles.CRM_MANAGER)) {
			final var customerMenu = CustomersMenu.build();
			mainMenu.addSubMenu(CUSTOMERS_OPTION, customerMenu);
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR)) {
			final var customerRepresentativeMenu = RepresentativesMenu.build();
			mainMenu.addSubMenu(CUSTOMER_REPRESENTATIVE_OPTION, customerRepresentativeMenu);
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER,ShodroneRoles.ADMIN,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER)) {
			final var showRequestMenu = ShowRequestMenu.build();
			mainMenu.addSubMenu(SHOW_REQUEST_OPTION, showRequestMenu);
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER)) {
			final var figureCategoryMenu = FigureCategoryMenu.build();
			final var figureMenu = FigureMenu.build();
			mainMenu.addSubMenu(FIGURE_OPTION, figureMenu);
			mainMenu.addSubMenu(FIGURE_CATEGORY_OPTION, figureCategoryMenu);
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER)) {
			final var showProposalMenu = ShowProposalMenu.build();
			mainMenu.addSubMenu(SHOW_PROPOSAL_OPTION, showProposalMenu);
		}

		if (!Application.settings().isMenuLayoutHorizontal()) {
			mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
		}

		if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
			final var settingsMenu = AdminMenu.build();
			mainMenu.addSubMenu(SETTINGS_OPTION, settingsMenu);
		}

		mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

		return mainMenu;
	}

}
