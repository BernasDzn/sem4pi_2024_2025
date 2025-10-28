package shodrone.core.domain.showrequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ShowRequestDescriptionTest {
    String path = "src/test/resources/";
    String name = "testDescription";
    ShowRequestDescription description;

    @BeforeEach
    void setUp() {
        description = new ShowRequestDescription(new File(path + name+".pdf"));
    }

    @Test
    void ensureShowRequestDescriptionCreation(){
        Blob blob;
        try {
            byte[] fileContent = Files.readAllBytes(new File(path + name+".pdf").toPath());
            blob = new SerialBlob(fileContent);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        ShowRequestDescription description2 = new ShowRequestDescription(blob);
        assertNotNull(description);
        assertNotNull(description2);

        //Used replace to avoid issues in GitHub actions, since File uses \
        assertEquals("src/test/resources/testDescription.pdf", description.blobToFile(name, path).getPath().replace("\\", "/"));
        assertEquals("src/test/resources/testDescription.pdf", description2.blobToFile(name, path).getPath().replace("\\", "/"));
    }

    @Test
    void ensureShowRequestDescriptionFileCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestDescription((File) null);
        });
    }

    @Test
    void ensureShowRequestDescriptionFileIsPdfOnly() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestDescription((new File(path + "testDescription.txt")));
        });
    }

    @Test
    void ensureShowRequestDescriptionFileCantBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequestDescription((new File(path)));
        });
    }
}