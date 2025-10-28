package shodrone.core.domain.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaceTest {
    Place place;

    @BeforeEach
    void setUp() {
        place = new Place("10,-15");
    }

    @Test
    void ensurePlaceCreation() {
        Place place2 = new Place(10.0, -15.0);
        assertNotNull(place);
        assertNotNull(place2);
        assertEquals("10.0, -15.0", place.toString());
        assertEquals("10.0, -15.0", place2.toString());
    }

    @Test
    void ensurePlaceCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Place(null);
        });
    }

    @Test
    void ensurePlaceCantBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Place("");
        });
    }

    @Test
    void ensurePlaceCantBeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Place("invalid place");
        });
    }
}