package shodrone.core.domain.showproposal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalTemplateTest {
    String filePathStr= "src/test/resources/Proposta_SP_01.txt";
    Path filePath = Path.of(filePathStr);
    File file= new File(filePathStr);
    Calendar date= Calendar.getInstance();


    ShowProposalTemplate template;

    @BeforeEach
    void setUp() {
        template = new ShowProposalTemplate(file, date ,filePath);
    }

    @Test
    void ensureConstructorCreatesValidTemplate() {
        assertNotNull(template);
        assertEquals(date.getTimeInMillis(), template.getAdditionDate().getTimeInMillis());
        assertTrue(template.getFilePath().contains("shodrone.sp_plugin") || template.getFilePath().endsWith("Proposta_SP_01.txt"));
    }

    @Test
    void ensureInvalidFileNotAccepted() {
        File invalidFile= new File("nonexistent.txt");
        Path path= invalidFile.toPath();
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalTemplate(invalidFile, date, path);
        });
    }

    @Test
    void EnsureblobToFileCreateOriginalContent() throws IOException {
        ShowProposalTemplate template = new ShowProposalTemplate(file, Calendar.getInstance(), filePath);

        File result = template.blobToFile("copiedFile", "src/test/resources/");

        assertTrue(result.exists());
        assertEquals(Files.readString(file.toPath()), Files.readString(result.toPath()));
    }
    @Test
    void ensureFutureDatesRejected() {
        date.add(Calendar.DAY_OF_MONTH, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposalTemplate(file, date, filePath);
        });
    }

}