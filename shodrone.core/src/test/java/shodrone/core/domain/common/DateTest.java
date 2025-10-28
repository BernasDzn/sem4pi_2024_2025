package shodrone.core.domain.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

class DateTest {
    Date date;

    @BeforeEach
    void setUp() {
        date = new Date("15/10/2026 12:15");
    }

    @Test
    void ensureDateCreation() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2026, Calendar.OCTOBER, 15, 12, 15);
        Date date2 = new Date(calendar);
        Date date3 = new Date(15, 10, 2026, 12, 15);
        Date date4 = new Date(15, 10, 2026);
        assertNotNull(date);
        assertNotNull(date2);
        assertNotNull(date3);
        assertNotNull(date4);
        assertEquals("15/10/2026 12:15", date.toString());
        assertEquals("15/10/2026 12:15", date2.toString());
        assertEquals("15/10/2026 12:15", date3.toString());
        assertEquals("15/10/2026 00:00", date4.toString());
    }

    @Test
    void ensureDateCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Date((Calendar) null);
        });
    }

    @Test
    void ensureDateCantBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Date("");
        });
    }

    @Test
    void ensureDateCantBeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Date("invalid date");
        });
    }
}