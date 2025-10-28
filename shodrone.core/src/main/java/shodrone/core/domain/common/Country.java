package shodrone.core.domain.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum Country {
    AL("Albania", "AL"),
    AD("Andorra", "AD"),
    AT("Austria", "AT"),
    BE("Belgium", "BE"),
    BA("Bosnia and Herzegovina", "BA"),
    BG("Bulgaria", "BG"),
    HR("Croatia", "HR"),
    CY("Cyprus", "CY"),
    CZ("Czech Republic", "CZ"),
    DK("Denmark", "DK"),
    EE("Estonia", "EE"),
    FI("Finland", "FI"),
    FR("France", "FR"),
    DE("Germany", "DE"),
    GR("Greece", "GR"),       // Also EL in VAT context
    HU("Hungary", "HU"),
    IS("Iceland", "IS"),
    IE("Ireland", "IE"),
    IT("Italy", "IT"),
    LV("Latvia", "LV"),
    LI("Liechtenstein", "LI"),
    LT("Lithuania", "LT"),
    LU("Luxembourg", "LU"),
    MT("Malta", "MT"),
    MD("Moldova", "MD"),
    MC("Monaco", "MC"),
    ME("Montenegro", "ME"),
    NL("Netherlands", "NL"),
    MK("North Macedonia", "MK"),
    NO("Norway", "NO"),
    PL("Poland", "PL"),
    PT("Portugal", "PT"),
    RO("Romania", "RO"),
    SM("San Marino", "SM"),
    RS("Serbia", "RS"),
    SK("Slovakia", "SK"),
    SI("Slovenia", "SI"),
    ES("Spain", "ES"),
    SE("Sweden", "SE"),
    CH("Switzerland", "CH"),
    TR("Turkey", "TR"),
    UA("Ukraine", "UA"),
    GB("United Kingdom", "GB"),
    VA("Vatican City", "VA"),
    EL("Greece", "EL"); // Also used in VAT context

    private final String countryName;
    private final String isoCode;

    Country(String countryName, String isoCode) {
        this.countryName = countryName;
        this.isoCode = isoCode;
    }

    /**
     * Get a Country using the ISO code
     * @param code
     * @return Country
     */
    public static Country fromIsoCode(String code) {
        for (Country country : values()) {
            if (country.getIsoCode().equalsIgnoreCase(code)) {
                return country;
            }
        }
        throw new IllegalArgumentException("No country in the system with code: " + code);
    }

    /**
     * Get a Country using the country name
     * @param countryName
     * @return Country
     */
    public static Country fromCountryName(String countryName) {
        for (Country country : values()) {
            if (country.getCountryName().equalsIgnoreCase(countryName)) {
                return country;
            }
        }
        throw new IllegalArgumentException("No country in the system with name: " + countryName);
    }

    /**
     * Gets all the countries in the enum
     * @return List of countries
     */
    public static List<Country> getAllCountries() {
        return new ArrayList<>(Arrays.asList(values()));
    }

    public static boolean isValid(String country) {
        try {
            fromCountryName(country);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(country + " is not a valid country.");
        }
        return true;
    }

}
