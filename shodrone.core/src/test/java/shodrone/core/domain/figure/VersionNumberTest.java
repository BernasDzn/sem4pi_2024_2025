package shodrone.core.domain.figure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionNumberTest {

    @Test
    void testValidVersionNumberCreation() {
        VersionNumber version = new VersionNumber("1.0.0");
        assertNotNull(version);
        assertEquals("1.0.0", version.toString());
    }

    @Test
    void testVersionNumberValueOf() {
        VersionNumber version= VersionNumber.valueOf("2.3.4");
        assertNotNull(version);
        assertEquals("2.3.4", version.toString());
    }

    @Test
    void testVersionNumberCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new VersionNumber(null);
        });
    }

    @Test
    void testVersionNumberCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new VersionNumber("");
        });
    }

    @Test
    void testVersionNumberEquality() {
        VersionNumber v1 = new VersionNumber("1.2.3");
        VersionNumber v2 = new VersionNumber("1.2.3");
        assertEquals(v1, v2);
    }

    @Test
    void testVersionNumberInequality() {
        VersionNumber v1 = new VersionNumber("1.0.0");
        VersionNumber v2 = new VersionNumber("2.0.0");
        assertNotEquals(v1, v2);
    }

    @Test
    void testVersionNumberCompareTo() {
        VersionNumber v1 = new VersionNumber("1.0.0");
        VersionNumber v2 = new VersionNumber("2.0.0");
         assertTrue(v1.compareTo(v2) < 0);
        assertTrue(v2.compareTo(v1) > 0);
        assertEquals(0, v1.compareTo(new VersionNumber("1.0.0")));
    }

    @Test
    void testVersionNumberToStringIsNotNull() {
        VersionNumber version = new VersionNumber("3.2.1");
        assertNotNull(version.toString());
    }

    @Test
    void testVersionNumberEqualsNull() {
        VersionNumber version = new VersionNumber("4.5.6");
        assertFalse(version.equals(null));
    }

    @Test
    void testVersionNumberEqualsDifferentType() {
        VersionNumber version = new VersionNumber("1.2.3");
        assertNotEquals(version, "1.2.3");
    }
}
