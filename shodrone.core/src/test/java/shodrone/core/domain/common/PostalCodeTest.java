package shodrone.core.domain.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostalCodeTest {

    final String postalCodeValue = "1234-567";
    final PostalCode postalCode = new PostalCode(Country.PT, postalCodeValue);

    @Test
    void ensurePostalCodeCantBeCreatedWithNullCountry() {
        assertThrows(Exception.class, () -> new PostalCode(null, postalCodeValue));
    }

    @Test
    void ensurePostalCodeCantBeCreatedWithNullPostalCode() {
        assertThrows(Exception.class, () -> new PostalCode(Country.PT, null));
    }

    @Test
    void ensurePostalCodeCantBeCreatedWithEmptyPostalCode() {
        assertThrows(Exception.class, () -> new PostalCode(Country.PT, ""));
    }

    @Test
    void ensurePostalCodeCantBeCreatedWithInvalidPostalCode() {
        assertThrows(Exception.class, () -> new PostalCode(Country.PT, "1233213123123"));
    }

}