package shodrone.core.domain.showproposal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalNumberOfDronesTest {
    ShowProposalNumberOfDrones numberOfDrones;

    @BeforeEach
    void setUp() {
        numberOfDrones = new ShowProposalNumberOfDrones(10);
    }

    @Test
    void ensureShowProposalNumberOfDronesCreation() {
        assertNotNull(numberOfDrones);
        assertEquals("10", numberOfDrones.toString());
    }

    @Test
    void ensureShowProposalNumberOfDronesCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalNumberOfDrones(0);
        });
    }

    @Test
    void ensureShowProposalNumberOfDronesCantBeLessThan1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalNumberOfDrones(-1);
        });
    }

    @Test
    void ensureShowProposalNumberOfDronesEquals() {
        ShowProposalNumberOfDrones numberOfDrones2 = new ShowProposalNumberOfDrones(10);
        assertEquals(numberOfDrones.toString(), numberOfDrones2.toString());
    }

    @Test
    void ensureShowProposalNumberOfDronesNotEquals() {
        ShowProposalNumberOfDrones numberOfDrones2 = new ShowProposalNumberOfDrones(20);
        assertNotEquals(numberOfDrones.toString(), numberOfDrones2.toString());
    }
}