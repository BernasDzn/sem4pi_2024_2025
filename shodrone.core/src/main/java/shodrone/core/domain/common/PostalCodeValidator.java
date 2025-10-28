package shodrone.core.domain.common;

import eapli.framework.validations.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PostalCodeValidator {

    private static final Map<String, Pattern> POSTAL_CODE_PATTERNS = new HashMap<>();

    static {
        POSTAL_CODE_PATTERNS.put(Country.PT.getIsoCode(), Pattern.compile("^\\d{4}-\\d{3}$")); // 1234-567
        POSTAL_CODE_PATTERNS.put(Country.ES.getIsoCode(), Pattern.compile("^\\d{5}$"));
        POSTAL_CODE_PATTERNS.put(Country.FR.getIsoCode(), Pattern.compile("^\\d{5}$"));
        POSTAL_CODE_PATTERNS.put(Country.DE.getIsoCode(), Pattern.compile("^\\d{5}$"));
        POSTAL_CODE_PATTERNS.put(Country.IT.getIsoCode(), Pattern.compile("^\\d{5}$"));
        POSTAL_CODE_PATTERNS.put(Country.NL.getIsoCode(), Pattern.compile("^\\d{4}\\s?[A-Z]{2}$")); // 1234 AB
        POSTAL_CODE_PATTERNS.put(Country.BE.getIsoCode(), Pattern.compile("^\\d{4}$"));
        POSTAL_CODE_PATTERNS.put(Country.LU.getIsoCode(), Pattern.compile("^L-\\d{4}$")); // Optional "L-"
        POSTAL_CODE_PATTERNS.put(Country.AT.getIsoCode(), Pattern.compile("^\\d{4}$"));
        POSTAL_CODE_PATTERNS.put(Country.CH.getIsoCode(), Pattern.compile("^\\d{4}$")); // Switzerland, if included
        POSTAL_CODE_PATTERNS.put(Country.SE.getIsoCode(), Pattern.compile("^\\d{3}\\s?\\d{2}$")); // 123 45
        POSTAL_CODE_PATTERNS.put(Country.FI.getIsoCode(), Pattern.compile("^\\d{5}$"));
        POSTAL_CODE_PATTERNS.put(Country.PL.getIsoCode(), Pattern.compile("^\\d{2}-\\d{3}$")); // 00-123
        POSTAL_CODE_PATTERNS.put(Country.IE.getIsoCode(), Pattern.compile("^[A-Z0-9]{3}\\s?[A-Z0-9]{4}$")); // Eircode
                                                                                                            // format
        POSTAL_CODE_PATTERNS.put("Default", Pattern.compile("^[A-Z0-9]{2,10}$")); // Default pattern for other countries
    }

    public static boolean isValid(Country country, String postalCode) {
        postalCode = postalCode.trim().toUpperCase();

        Preconditions.nonNull(country, "Cannot validate postal code for null country");
        Preconditions.nonNull(postalCode, "Cannot validate null postal code");

        Pattern pattern;
        try {
            pattern = POSTAL_CODE_PATTERNS.get(country.getIsoCode());
        } catch (IllegalArgumentException e) {
            pattern = POSTAL_CODE_PATTERNS.get("Default");
        }

        Preconditions.nonNull(pattern, "Cannot validate postal code for unknown country");

        if (!pattern.matcher(postalCode).matches()) {
            throw new IllegalArgumentException(postalCode + " is not a valid postal code for " + country.getIsoCode());
        }

        return true;
    }

}
