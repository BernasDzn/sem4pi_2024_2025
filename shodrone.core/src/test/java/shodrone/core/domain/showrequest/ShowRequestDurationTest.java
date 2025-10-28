package shodrone.core.domain.showrequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowRequestDurationTest {
    ShowRequestDuration duration;

    @BeforeEach
    void setUp() {
        duration = new ShowRequestDuration(15);
    }

    @Test
    void ensureShowRequestDurationCreation() {
        assertNotNull(duration);
        assertEquals("15m", duration.toString());
    }

    @Test
    void ensureShowRequestDurationCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestDuration(0);
        });
    }

    @Test
    void ensureShowRequestDurationCantBeLessThan1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestDuration(-1);
        });
    }

    @Test
    void ensureShowRequestDurationEquals() {
        ShowRequestDuration duration2 = new ShowRequestDuration(15);
        assertEquals(duration.toString(), duration2.toString());
    }

    @Test
    void ensureShowRequestDurationNotEquals() {
        ShowRequestDuration duration2 = new ShowRequestDuration(20);
        assertNotEquals(duration.toString(), duration2.toString());
    }

}