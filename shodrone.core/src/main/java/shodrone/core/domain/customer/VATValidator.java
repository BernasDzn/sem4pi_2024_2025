package shodrone.core.domain.customer;

import eapli.framework.validations.Preconditions;
import shodrone.core.domain.common.Country;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public class VATValidator {

    private static final Map<Country, Pattern> VAT_PATTERNS = new EnumMap<>(Country.class);

    static {
        VAT_PATTERNS.put(Country.AT, Pattern.compile("^U\\d{8}$"));
        VAT_PATTERNS.put(Country.BE, Pattern.compile("^\\d{10}$"));
        VAT_PATTERNS.put(Country.BG, Pattern.compile("^\\d{9,10}$"));
        VAT_PATTERNS.put(Country.HR, Pattern.compile("^\\d{11}$"));
        VAT_PATTERNS.put(Country.CY, Pattern.compile("^\\d{8}[A-Z]$"));
        VAT_PATTERNS.put(Country.CZ, Pattern.compile("^\\d{8,10}$"));
        VAT_PATTERNS.put(Country.DK, Pattern.compile("^\\d{8}$"));
        VAT_PATTERNS.put(Country.EE, Pattern.compile("^\\d{9}$"));
        VAT_PATTERNS.put(Country.EL, Pattern.compile("^\\d{9}$")); // Greece
        VAT_PATTERNS.put(Country.ES, Pattern.compile("^[A-Z0-9]\\d{7}[A-Z0-9]$"));
        VAT_PATTERNS.put(Country.FI, Pattern.compile("^\\d{8}$"));
        VAT_PATTERNS.put(Country.FR, Pattern.compile("^[A-Z0-9]{2}\\d{9}$"));
        VAT_PATTERNS.put(Country.DE, Pattern.compile("^\\d{9}$"));
        VAT_PATTERNS.put(Country.HU, Pattern.compile("^\\d{8}$"));
        VAT_PATTERNS.put(Country.IE, Pattern.compile("^(\\d{7}[A-W])|(\\d{7}[A-W][A-I])$"));
        VAT_PATTERNS.put(Country.IT, Pattern.compile("^\\d{11}$"));
        VAT_PATTERNS.put(Country.LV, Pattern.compile("^\\d{11}$"));
        VAT_PATTERNS.put(Country.LT, Pattern.compile("^(\\d{9}|\\d{12})$"));
        VAT_PATTERNS.put(Country.LU, Pattern.compile("^\\d{8}$"));
        VAT_PATTERNS.put(Country.MT, Pattern.compile("^\\d{8}$"));
        VAT_PATTERNS.put(Country.NL, Pattern.compile("^\\d{9}B\\d{2}$"));
        VAT_PATTERNS.put(Country.PL, Pattern.compile("^\\d{10}$"));
        VAT_PATTERNS.put(Country.PT, Pattern.compile("^\\d{9}$"));
        VAT_PATTERNS.put(Country.RO, Pattern.compile("^\\d{2,10}$"));
        VAT_PATTERNS.put(Country.SK, Pattern.compile("^\\d{10}$"));
        VAT_PATTERNS.put(Country.SI, Pattern.compile("^\\d{8}$"));
        VAT_PATTERNS.put(Country.SE, Pattern.compile("^\\d{12}$"));

        // Optional: add GB, NO, CH, etc.
    }

    public static boolean isValid(Country country, String uniqueNumber) {
        Preconditions.nonNull(country, "Country code cannot be null");
        Preconditions.nonNull(uniqueNumber, "Unique number cannot be null");
        Pattern pattern;
        try{
            pattern = VAT_PATTERNS.get(country);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid country code " + country);
        }
        if(!pattern.matcher(uniqueNumber).matches()) {
            throw new IllegalArgumentException("Invalid VAT number for country " + country);
        }
        return true;
    }
}
