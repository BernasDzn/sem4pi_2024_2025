package shodrone.core.domain.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    final String streetName = "Main St";
    final int streetNumber = 123;
    final String city = "Lisbon";
    final String country = "Portugal";
    final String postalCodeValue = "1234-567";
    final PostalCode postalCode = new PostalCode(Country.PT, "1234-567");

    @Test
    void ensureAddressCantBeCreatedWithNullStreet() {
        assertThrows(Exception.class, () -> new Address(null, streetNumber, postalCodeValue, city, country));
    }

    @Test
    void ensureAddressCantBeCreatedWithEmptyStreet() {
        assertThrows(Exception.class, () -> new Address("", streetNumber, postalCodeValue, city, country));
    }

    @Test
    void ensureAddressCantBeCreatedWithNegativeStreetNumber() {
        assertThrows(Exception.class, () -> new Address(streetName, -1, postalCodeValue, city, country));
    }

    @Test
    void ensureAddressCantBeCreatedWithNullPostalCode() {
        assertThrows(Exception.class, () -> new Address(streetName, streetNumber, null, country, city));
    }

    @Test
    void ensureAddressCantBeCreatedWithEmptyPostalCode() {
        assertThrows(Exception.class, () -> new Address(streetName, streetNumber, "", country, city));
    }

    @Test
    void ensureAddressCannotBeCreatedWithInvalidPostalCodeForCountry() {
        assertThrows(Exception.class, () -> new Address(streetName, streetNumber, "1233213123123", country, city));
    }

    @Test
    void ensureAddressCannotBeCreatedWithNullCountry() {
        assertThrows(Exception.class, () -> new Address(streetName, streetNumber, postalCodeValue, null, city));
    }

    @Test
    void ensureAddressCannotBeCreatedWithInvalidCountry() {
        assertThrows(Exception.class, () -> new Address(streetName, streetNumber, postalCodeValue, "fictional country", city));
    }

    @Test
    void ensureAddressCannotBeCreatedWithInvalidCity() {
        assertThrows(Exception.class, () -> new Address(streetName, streetNumber, postalCodeValue, country, null));
    }

}