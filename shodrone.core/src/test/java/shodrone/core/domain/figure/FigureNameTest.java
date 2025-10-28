package shodrone.core.domain.figure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FigureNameTest {

    @Test
    void testValidFigureNameCreation() {
        FigureName figureName = new FigureName("Arrow");
        assertNotNull(figureName);
        assertEquals("Arrow", figureName.toString());
    }

    @Test
    void testCreatingFigureNameWithNullShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureName(null);
        });
    }

    @Test
    void testFigureNameEquality() {
        FigureName name1 = new FigureName("FigureA");
        FigureName name2 = new FigureName("FigureA");
        assertEquals(name1, name2);
    }

    @Test
    void testFigureNameInequality() {
        FigureName name1 = new FigureName("FigureA");
        FigureName name2 = new FigureName("FigureB");
        assertNotEquals(name1, name2);
    }

    @Test
    void testCompareToSmaller() {
        FigureName name1 = new FigureName("Fig");
        FigureName name2 = new FigureName("Figure");
        assertTrue(name1.compareTo(name2) < 0);
    }

    @Test
    void testCompareToGreater() {
        FigureName name1 = new FigureName("Figure");
        FigureName name2 = new FigureName("Fig");
        assertTrue(name1.compareTo(name2) > 0);
    }

    @Test
    void testCompareToEqual() {
        FigureName name1 = new FigureName("Same");
        FigureName name2 = new FigureName("Same");
        assertEquals(0, name1.compareTo(name2));
    }

    @Test
    void testToStringIsNotNull() {
        FigureName name = new FigureName("Something");
        assertNotNull(name.toString());
    }

    @Test
    void testEqualsWithNull() {
        FigureName name = new FigureName("FigureX");
        assertFalse(name.equals(null));
    }

    @Test
    void testEmptyOrNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new FigureName(""));
        assertThrows(IllegalArgumentException.class, () -> new FigureName(null));
    }
}
