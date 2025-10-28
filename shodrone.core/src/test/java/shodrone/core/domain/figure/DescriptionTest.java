package shodrone.core.domain.figure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionTest {

    @Test
    void testValidDescriptionCreation() {
        Description desc = new Description("A valid figure description");
        assertNotNull(desc);
        assertEquals("A valid figure description", desc.toString());
    }

    @Test
    void testDescriptionValueOf() {
        Description desc = Description.valueOf("description");
        assertNotNull(desc);
        assertEquals("description", desc.toString());
    }

    @Test
    void testDescriptionCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Description(null);
        });
    }

    @Test
    void testDescriptionCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Description("");
        });
    }

    @Test
    void testDescriptionEquality() {
        Description d1 = new Description("Same description");
        Description d2 = new Description("Same description");
        assertEquals(d1, d2);
    }

    @Test
    void testDescriptionInequality() {
        Description d1=new Description("Description1");
        Description d2=new Description("Description2");
        assertNotEquals(d1, d2);
    }

    @Test
    void testDescriptionCompareTo() {
        Description d1=new Description("Arrow");
        Description d2=new Description("Bow");
        assertTrue(d1.compareTo(d2) <0);
        assertTrue(d2.compareTo(d1) >0);
        assertEquals(0, d1.compareTo(new Description("Arrow")));
    }

    @Test
    void testDescriptionToStringIsNotNull() {
        Description d= new Description("Desc");
        assertNotNull(d.toString());
    }

    @Test
    void testDescriptionEqualsNull() {
        Description d= new Description("Not null");
        assertFalse(d.equals(null));
    }

    @Test
    void testDescriptionEqualsDifferentType() {
        Description d = new Description("Description");
        assertNotEquals(d, "Description");
    }
}
