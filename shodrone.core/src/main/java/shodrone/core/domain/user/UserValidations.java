package shodrone.core.domain.user;

import eapli.framework.validations.Preconditions;

import java.util.regex.Pattern;

public class UserValidations {

    static Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");

    public static void validateUsername(final String username) {
        Preconditions.nonNull(username, "Username cannot be null");
        if(username.length() < 3 || username.length() > 25) {
            throw new IllegalArgumentException("Username must be between 3 and 25 characters");
        }
        Preconditions.matches(pattern, username, "Username can only contain letters, numbers and underscores (_)");
    }

    public static void validatePassword(String password) {
        ShodronePasswordPolicy policy = new ShodronePasswordPolicy();
        Preconditions.nonEmpty(password, "Password cannot be empty");
        if(!policy.isSatisfiedBy(password)) {
            throw new IllegalArgumentException("Password must be at least 6 characters long and contain at least one uppercase letter and one digit.");
        }
    }
}
