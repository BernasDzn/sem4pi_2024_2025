package shodrone.core.domain.showproposal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalDescriptionTest {
    String path = "src/test/resources/";
    String name = "testDescription";
    ShowProposalDescription description;

    @BeforeEach
    void setUp() {
        description = new ShowProposalDescription(new File(path + name+".pdf"));
    }

    @Test
    void ensureShowProposalDescriptionCreation(){
        Blob blob;
        try {
            byte[] fileContent = Files.readAllBytes(new File(path + name+".pdf").toPath());
            blob = new SerialBlob(fileContent);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        ShowProposalDescription description2 = new ShowProposalDescription(blob);
        assertNotNull(description);
        assertNotNull(description2);

        //Used replace to avoid issues in GitHub actions, since File uses \
        assertEquals("src/test/resources/testDescription.pdf", description.blobToFile(name, path).getPath().replace("\\", "/"));
        assertEquals("src/test/resources/testDescription.pdf", description2.blobToFile(name, path).getPath().replace("\\", "/"));
    }

    @Test
    void ensureShowProposalDescriptionFileCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalDescription((File) null);
        });
    }

    @Test
    void ensureShowProposalDescriptionFileIsPdfOnly() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalDescription((new File(path + "testDescription.txt")));
        });
    }

    @Test
    void ensureShowProposalDescriptionFileCantBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalDescription((new File(path)));
        });
    }
}