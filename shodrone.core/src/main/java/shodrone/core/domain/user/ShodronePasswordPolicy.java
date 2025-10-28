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
package shodrone.core.domain.user;

import eapli.framework.infrastructure.authz.domain.model.PasswordPolicy;
import eapli.framework.strings.util.StringPredicates;

public class ShodronePasswordPolicy implements PasswordPolicy {

	// ===== Developer Statement =====
	// The password policy is not defined in the system requirements.
	// So it will follow normal and common password policies. such as:
	// - at least 6 characters long
	// - at least one number
	// - at least one capital letter

	@Override
	public boolean isSatisfiedBy(final String rawPassword) {

		// at least 6 characters long
		// at least one digit
		if (StringPredicates.isNullOrEmpty(rawPassword) || (rawPassword.length() < 6) || !StringPredicates.containsDigit(rawPassword)) {
			return false;
		}

		// at least one capital letter
		return StringPredicates.containsCapital(rawPassword);
	}

	/**
	 * Check how strong a password is. just for demo purposes.
	 *
	 * <p>
	 * look into
	 * https://documentation.cpanel.net/display/CKB/How+to+Determine+Password+Strength
	 * for example rules of password strength
	 *
	 * @param rawPassword the string to check
	 *
	 * @return how strong a password is
	 */
	public PasswordStrength strength(final String rawPassword) {
		var passwordStrength = PasswordStrength.WEAK;
		if (rawPassword.length() >= 12
				|| (rawPassword.length() >= 8 && StringPredicates.containsAny(rawPassword, "$#!%&?"))) {
			passwordStrength = PasswordStrength.EXCELENT;
		} else if (rawPassword.length() >= 8) {
			passwordStrength = PasswordStrength.GOOD;
		} else if (rawPassword.length() >= 6) {
			passwordStrength = PasswordStrength.WEAK;
		}
		return passwordStrength;
	}

	public enum PasswordStrength {
		WEAK, GOOD, EXCELENT,
	}
}
