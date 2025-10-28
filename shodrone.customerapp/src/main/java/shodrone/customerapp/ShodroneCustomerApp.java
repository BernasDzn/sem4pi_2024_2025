package shodrone.customerapp;

import eapli.framework.infrastructure.pubsub.EventDispatcher;
import shodrone.core.application.user.eventhandlers.SignupAcceptedWatchDog;
import shodrone.core.domain.utente.events.SignupAcceptedEvent;
import shodrone.core.presentation.console.BaseApp;
import shodrone.customerapp.presentation.LoginUI;
import shodrone.customerapp.presentation.MainMenu;

public class ShodroneCustomerApp extends BaseApp {

	/**
	 * dont instantiate this class.
	 */
	private ShodroneCustomerApp() {}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new ShodroneCustomerApp().run(args);
	}

	@Override
	protected void doMain(String[] args) {
		LoginUI loginUI = new LoginUI();
		if (loginUI.show()) {
			final var menu = new MainMenu();
			menu.mainLoop();
		}
	}

	@Override
	protected String appTitle() {
		return "Shodrone Customer Application";
	}

	@Override
	protected String appGoodbye() {
		return "Thank you for using Shodrone Customer Application";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetupEventHandlers(final EventDispatcher dispatcher) {
		dispatcher.subscribe(new SignupAcceptedWatchDog(), SignupAcceptedEvent.class);
	}
	
}
