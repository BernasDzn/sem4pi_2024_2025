package shodrone.core.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAttribute;

import java.io.Serial;

@Embeddable
public class Address implements ValueObject, Comparable<Address> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The street name of the address
     */
    @XmlAttribute
    @JsonProperty
    private String streetName;

    /**
     * The number of the address
     */
    @XmlAttribute
    @JsonProperty
    private int addressNumber;

    /**
     * The postal code of the address
     */
    @XmlAttribute
    @JsonProperty
    private PostalCode postalCode;

    /**
     * The country of the address
     */
    @XmlAttribute
    @JsonProperty
    @Enumerated(EnumType.STRING)
    private Country country;

    /**
     * The city of the address
     */
    @XmlAttribute
    @JsonProperty
    private String city;

    public Address() {} // for ORM

    /**
     * Constructor for Address
     * @param streetName
     * @param addressNumber
     * @param postalCode
     * @param country
     * @param city
     */
    public Address(String streetName, int addressNumber, String postalCode, String country, String city) {

        Preconditions.noneNull(streetName, addressNumber, postalCode, country, city);
        isValidStreetName(streetName);
        isValidAddressNumber(addressNumber);
        isValidPostalCode(Country.fromCountryName(country), postalCode);
        isValidCountry(country);
        isValidCity(city);

        this.streetName = streetName;
        this.addressNumber = addressNumber;
        this.postalCode = new PostalCode(Country.fromCountryName(country), postalCode);
        this.country = Country.fromCountryName(country);
        this.city = city;
    }

    @Override
    public String toString() {
        return streetName + ", nº " + addressNumber + ", " + postalCode + " " + city + ", " + country.getCountryName();
    }

    @Override
    public int compareTo(Address o) {
        int result = this.streetName.compareTo(o.streetName);
        if (result != 0) {
            return result;
        }
        result = Integer.compare(this.addressNumber, o.addressNumber);
        if (result != 0) {
            return result;
        }
        result = this.postalCode.compareTo(o.postalCode);
        if (result != 0) {
            return result;
        }
        result = this.country.compareTo(o.country);
        if (result != 0) {
            return result;
        }
        return this.city.compareTo(o.city);
    }


    // ========== Validation Section ===========
    // These methods are public and can be called anywhere in the code,
    // therefore, it is up to the caller to catch the exceptions.
    // This also means the UI can call these methods to validate the inputs.

    /**
     * Validates the street name, used for UI and Constructor.
     * Throws an exception if the street name is not valid. Therefore, it must be caught somewhere.
     * @param streetName
     * @return
     */
    public static boolean isValidStreetName(String streetName) {
        return StringValidator.isValid(streetName, "Street name");
    }

    /**
     * Validates the address number, used for UI and Constructor.
     * Throws an exception if the address number is not valid. Therefore, it must be caught somewhere.
     * @param addressNumber
     * @return
     */
    public static boolean isValidAddressNumber(int addressNumber) {
        Preconditions.isPositive(addressNumber);
        return true;
    }

    /**
     * Validates the postal code, used for UI and Constructor.
     * Throws an exception if the postal code is not valid. Therefore, it must be caught somewhere.
     * @param country
     * @param postalCode
     * @return
     */
    public static boolean isValidPostalCode(Country country, String postalCode) {
        return PostalCodeValidator.isValid(country, postalCode);
    }

    /**
     * Validates the country, used for UI and Constructor.
     * Throws an exception if the country is not valid. Therefore, it must be caught somewhere.
     * @param country
     * @return
     */
    public static boolean isValidCountry(String country) {
        return Country.isValid(country);
    }

    /**
     * Validates the city, used for UI and Constructor.
     * Throws an exception if the city is not valid. Therefore, it must be caught somewhere.
     * @param city
     * @return
     */
    public static boolean isValidCity(String city) {
        return StringValidator.isValid(city, "City");
    }

}
