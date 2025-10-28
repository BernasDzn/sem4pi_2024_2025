package shodrone.core.domain.showrequest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class ShowRequestNumberOfDronesTest {
    ShowRequestNumberOfDrones numberOfDrones;

    @BeforeEach
    void setUp() {
        numberOfDrones = new ShowRequestNumberOfDrones(10);
    }

    @Test
    void ensureShowRequestNumberOfDronesCreation() {
        assertNotNull(numberOfDrones);
        assertEquals("10", numberOfDrones.toString());
    }

    @Test
    void ensureShowRequestNumberOfDronesCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestNumberOfDrones(0);
        });
    }

    @Test
    void ensureShowRequestNumberOfDronesCantBeLessThan1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestNumberOfDrones(-1);
        });
    }

    @Test
    void ensureShowRequestNumberOfDronesEquals() {
        ShowRequestNumberOfDrones numberOfDrones2 = new ShowRequestNumberOfDrones(10);
        assertEquals(numberOfDrones.toString(), numberOfDrones2.toString());
    }

    @Test
    void ensureShowRequestNumberOfDronesNotEquals() {
        ShowRequestNumberOfDrones numberOfDrones2 = new ShowRequestNumberOfDrones(20);
        assertNotEquals(numberOfDrones.toString(), numberOfDrones2.toString());
    }
}