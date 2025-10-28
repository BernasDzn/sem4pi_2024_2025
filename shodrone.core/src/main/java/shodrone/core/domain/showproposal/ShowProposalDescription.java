package shodrone.core.domain.showproposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlElement;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.Blob;

@Embeddable
public class ShowProposalDescription implements ValueObject {
    @XmlElement
    @JsonProperty
    private Blob descriptionDocument;


    public ShowProposalDescription() {
    }

    public ShowProposalDescription(File descriptionFile) {
        validate(descriptionFile);

        this.descriptionDocument = fileToBlob(descriptionFile);
    }

    public ShowProposalDescription(Blob descriptionDocument) {
        Preconditions.nonNull(descriptionDocument);
        this.descriptionDocument = descriptionDocument;
    }

    private Blob fileToBlob(File descriptionFile) {
        validate(descriptionFile);

        Blob blob;
        try {
            byte[] fileContent = Files.readAllBytes(descriptionFile.toPath());
            blob = new SerialBlob(fileContent);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating blob from file");
        }

        return blob;
    }

    public File blobToFile(String name, String path) {
        Preconditions.noneNull(this.descriptionDocument, path, name);
        File file;
        try{
            file = new File(path + name + ".pdf");
            if (!file.exists()) {
                boolean created = file.createNewFile();

                if (!created) {
                    throw new IllegalArgumentException("Error creating file");
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] blobContent = this.descriptionDocument.getBytes(1, (int) this.descriptionDocument.length());
            fos.write(blobContent);
            fos.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating file from blob");
        }
        return file;
    }

    public static void validate(File descriptionDocument) {
        Preconditions.nonNull(descriptionDocument);
        if (!descriptionDocument.exists()) {
            throw new IllegalArgumentException("The file does not exist");
        }
        if (!descriptionDocument.canRead()) {
            throw new IllegalArgumentException("The file is not readable");
        }

        if (descriptionDocument.length() > 1048576) {
            throw new IllegalArgumentException("The file is too large");
        }

        if (descriptionDocument.length() == 0) {
            throw new IllegalArgumentException("The file is empty");
        }

        if (!descriptionDocument.getName().endsWith(".pdf")) {
            throw new IllegalArgumentException("The file is not a pdf file");
        }
    }
}
