package shodrone.core.domain.showproposal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalDurationTest {
    ShowProposalDuration duration;

    @BeforeEach
    void setUp() {
        duration = new ShowProposalDuration(15);
    }

    @Test
    void ensureShowProposalDurationCreation() {
        assertNotNull(duration);
        assertEquals("15m", duration.toString());
    }

    @Test
    void ensureShowProposalDurationCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalDuration(0);
        });
    }

    @Test
    void ensureShowProposalDurationCantBeLessThan1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalDuration(-1);
        });
    }

    @Test
    void ensureShowProposalDurationEquals() {
        ShowProposalDuration duration2 = new ShowProposalDuration(15);
        assertEquals(duration.toString(), duration2.toString());
    }

    @Test
    void ensureShowProposalDurationNotEquals() {
        ShowProposalDuration duration2 = new ShowProposalDuration(20);
        assertNotEquals(duration.toString(), duration2.toString());
    }

}