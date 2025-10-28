package shodrone.core.domain.common;

import eapli.framework.validations.Preconditions;

public class StringValidator {

    private static final int MAX_DEFAULT_LENGHT = 250;

    public static boolean isValid(String stringToValidate, String context, int maxLength) {

        Preconditions.nonEmpty(stringToValidate, context + " cannot be empty.");

        if (stringToValidate.length() > maxLength) {
            throw new IllegalArgumentException(
                    context + " is too long. Maximum length is " + maxLength + " characters.");
        }

        return true;
    }

    public static boolean isValid(String designation, String context) {
        return isValid(designation, context, MAX_DEFAULT_LENGHT);
    }

}
