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
package shodrone.backoffice;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import shodrone.backoffice.presentation.MainMenu;
import shodrone.core.application.user.eventhandlers.SignupAcceptedWatchDog;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.domain.utente.events.SignupAcceptedEvent;
import shodrone.core.infrastructure.auth.AuthenticationCredentialHandler;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.presentation.console.BaseApp;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.core.presentation.console.auth.LoginUI;

/**
 *
 * @author Paulo Gandra Sousa
 */
@SuppressWarnings("squid:S106")
public final class ShodroneBackoffice extends BaseApp {


	/**
	 * dont instantiate this class.
	 */
	private ShodroneBackoffice() {
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(final String[] args) {

		AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
				new ShodronePasswordEncoder());

		new ShodroneBackoffice().run(args);
	}

	@Override
	protected void doMain(final String[] args) {
		// login and go to main menu
		if (new LoginUI(new AuthenticationCredentialHandler()).show()) {
			if(AuthzRegistry.authorizationService().session().get().authenticatedUser().hasAny(ShodroneRoles.CUSTOMER)){
				ConsoleEvent.error("You are not allowed to access this application");
				return;
			}
			final var menu = new MainMenu();
			menu.mainLoop();
		}
	}

	@Override
	protected String appTitle() {
		return "Back Office";
	}

	@Override
	protected String appGoodbye() {
		return "Back Office";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetupEventHandlers(final EventDispatcher dispatcher) {
		dispatcher.subscribe(new SignupAcceptedWatchDog(), SignupAcceptedEvent.class);
	}
}
