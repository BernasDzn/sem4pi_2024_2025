package shodrone.core.domain.showrequest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;

import java.io.File;

class ShowRequestVersionTest {
    ShowRequestVersion version;
    ShowRequestNumberOfDrones numberOfDrones;
    ShowRequestDuration duration;
    ShowRequestDescription description;
    String path = "src/test/resources/";
    String name = "testDescription";
    Place place;
    Date date;
    ShowRequestStatus status;

    @BeforeEach
    void setUp() {
        version = new ShowRequestVersion(
                new ShowRequestNumberOfDrones(10),
                new ShowRequestDuration(15),
                new ShowRequestDescription(new File(path + name+".pdf")),
                new Place("10,-15"),
                new Date("15/10/2026 12:15"),
                ShowRequestStatus.NEW
        );
    }

    @Test
    void ensureShowRequestVersionCreation() {
        assertNotNull(version);
        assertEquals("10", version.getNumberOfDrones().toString());
        assertEquals("15m", version.getDuration().toString());
        assertEquals(name+".pdf", version.getDescription().blobToFile(name,path).getName());
        assertEquals("10.0, -15.0", version.getPlace().toString());
        assertEquals("15/10/2026 12:15", version.getDate().toString());
        assertEquals(ShowRequestStatus.NEW, version.getStatus());
    }

    @Test
    void ensureShowRequestVersionCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(null, null, null, null, null, null);
        });
    }

    @Test
    void ensureShowRequestVersionNumberOfDronesCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(null, duration, description, place, date, status);
        });
    }

    @Test
    void ensureShowRequestVersionDurationCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(numberOfDrones, null, description, place, date, status);
        });
    }

    @Test
    void ensureShowRequestVersionDescriptionCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(numberOfDrones, duration, null, place, date, status);
        });
    }

    @Test
    void ensureShowRequestVersionPlaceCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(numberOfDrones, duration, description, null, date, status);
        });
    }

    @Test
    void ensureShowRequestVersionDateCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(numberOfDrones, duration, description, place, null, status);
        });
    }

    @Test
    void ensureShowRequestVersionStatusCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestVersion(numberOfDrones, duration, description, place, date, null);
        });
    }
}