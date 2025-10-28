package shodrone.core.domain.customer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VATNumberTest {

    @Test
    void testPortugueseVATNumberCreation() {
        VATNumber vatNumber = new VATNumber("PT123456789");
        assertNotNull(vatNumber);
        assertEquals("PT123456789", vatNumber.toString());
    }

    @Test
    void testSpanishVATNumberCreation(){
        VATNumber vatNumber = new VATNumber("ES12345678A");
        assertNotNull(vatNumber);
        assertEquals("ES12345678A", vatNumber.toString());
    }

    @Test
    void testVATNumberInvalidCountry() {
        assertThrows(IllegalArgumentException.class, () -> {
            new VATNumber("INVALID123456789");
        });
    }

    @Test
    void testVATNumberInvalidUniqueNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new VATNumber("PT");
        });
        String expectedMessage = "Invalid VAT number for country PT";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testVATNumberEquals() {
        VATNumber vatNumber1 = new VATNumber("PT123456789");
        VATNumber vatNumber2 = new VATNumber("PT123456789");
        assertEquals(vatNumber1, vatNumber2);
    }

    @Test
    void testVATNumberNotEquals() {
        VATNumber vatNumber1 = new VATNumber("PT123456789");
        VATNumber vatNumber2 = new VATNumber("PT987654321");
        assertNotEquals(vatNumber1, vatNumber2);
    }

    @Test
    void testVATNumberCompareTo() {
        VATNumber vatNumber1 = new VATNumber("PT123456789");
        VATNumber vatNumber2 = new VATNumber("PT987654321");
        assertTrue(vatNumber1.compareTo(vatNumber2) < 0);
        assertTrue(vatNumber2.compareTo(vatNumber1) > 0);
    }

    @Test
    void testVATNumberToString() {
        VATNumber vatNumber = new VATNumber("PT123456789");
        assertEquals("PT123456789", vatNumber.toString());
    }

    @Test
    void testVATNumberNullEquals() {
        VATNumber vatNumber = new VATNumber("PT123456789");
        assertFalse(vatNumber.equals(null));
    }

    @Test
    void testVATNumberNullToString() {
        VATNumber vatNumber = new VATNumber("PT123456789");
        assertNotNull(vatNumber.toString());
    }

}