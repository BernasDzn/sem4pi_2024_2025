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
package shodrone.core.presentation.console.auth;

import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.domain.user.UserValidations;
import shodrone.core.infrastructure.auth.CredentialHandler;
import shodrone.core.presentation.console.ConsoleEvent;

/**
 * UI for user login action.
 *
 */
@SuppressWarnings("squid:S106")
public class LoginUI extends AbstractUI {

	private Role onlyWithThis;
	// TODO: This should be a configuration parameter
	private static final int DEFAULT_MAX_ATTEMPTS = 3;
	private final int maxAttempts;

	/**
	 * The credential handler, which is responsible for the authentication.
	 */
	private final CredentialHandler credentialHandler;

	/**
	 * Constructor for LoginUI with a credential handler only.
	 * 
	 * @param credentialHandler
	 */
	public LoginUI(CredentialHandler credentialHandler) {
		maxAttempts = DEFAULT_MAX_ATTEMPTS;
		this.credentialHandler = credentialHandler;
	}

	/**
	 * Constructor for LoginUI with a credential handler and a role.
	 * 
	 * @param credentialHandler
	 * @param onlyWithThis
	 */
	public LoginUI(CredentialHandler credentialHandler, final Role onlyWithThis) {
		this.onlyWithThis = onlyWithThis;
		maxAttempts = DEFAULT_MAX_ATTEMPTS;
		this.credentialHandler = credentialHandler;
	}

	/**
	 * Constructor for LoginUI with a credential handler, a role and a chosen
	 * maximum number of attempts.
	 * 
	 * @param credentialHandler
	 * @param onlyWithThis
	 * @param maxAttempts
	 */
	public LoginUI(CredentialHandler credentialHandler, final Role onlyWithThis, final int maxAttempts) {
		this.onlyWithThis = onlyWithThis;
		this.maxAttempts = maxAttempts;
		this.credentialHandler = credentialHandler;
	}

	/**
	 * Constructor for LoginUI with a credential handler and a chosen maximum number
	 * of attempts.
	 * 
	 * @param credentialHandler
	 * @param maxAttempts
	 */
	public LoginUI(CredentialHandler credentialHandler, final int maxAttempts) {
		this.maxAttempts = maxAttempts;
		this.credentialHandler = credentialHandler;
	}

	@Override
	protected boolean doShow() {
		var attempt = 1;

		while (attempt <= maxAttempts) { // 3 attempts to login with correct credentials

			String userName = askUserName();
			String password = askPassword();

			if (credentialHandler.authenticated(userName, password, onlyWithThis)) {
				return true;
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