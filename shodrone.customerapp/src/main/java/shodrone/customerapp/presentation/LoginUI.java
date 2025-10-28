package shodrone.customerapp.presentation;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.domain.user.UserValidations;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.customerapp.CustomerAppCli;

/**
 * UI for user login action.
 *
 */
@SuppressWarnings("squid:S106")
public class LoginUI extends AbstractUI {

	private static final int DEFAULT_MAX_ATTEMPTS = 3;
	private final int maxAttempts;

	/**
	 * Constructor for LoginUI with a credential handler only.
	 * 
	 * @param credentialHandler
	 */
	public LoginUI() {
		maxAttempts = DEFAULT_MAX_ATTEMPTS;
	}

	@Override
	protected boolean doShow() {
		var attempt = 1;

		while (attempt <= maxAttempts) { // 3 attempts to login with correct credentials

			String userName = askUserName();
			String password = askPassword();

			try{
				CustomerAppCli.getInstance();
				if(CustomerAppCli.tryLogin(userName, password)){
					return true;
				}
			}catch (Exception e) {
				ConsoleEvent.error("Cannot connect to the server. Please try again later.");
				return false;
			}
			ConsoleEvent.error("Wrong username or password. You have " + (maxAttempts - attempt)
					+ " attempts left.%n%n»»»»»»»»»%n");
			attempt++;
		}
		ConsoleEvent.error("Sorry, we are unable to authenticate you. Please contact your system admnistrator.");
		return false;
	}

	protected String askUserName() {
		boolean allOk = false;
		String userName = "";
		while (!allOk) {
			try {
				userName = Console.readLine("Please provide a username: ");
				UserValidations.validateUsername(userName);
				allOk = true;
			} catch (Exception e) {
				ConsoleEvent.error(e.getMessage());
			}
		}
		return userName;
	}

	protected String askPassword() {
		boolean allOk = false;
		String password = "";
		while (!allOk) {
			try {
				password = Console.readLine("Please provide a password: ");
				UserValidations.validatePassword(password);
				allOk = true;
			} catch (Exception e) {
				ConsoleEvent.error(e.getMessage());
			}
		}
		return password;
	}

	@Override
	public String headline() {
		return "Login";
	}
}