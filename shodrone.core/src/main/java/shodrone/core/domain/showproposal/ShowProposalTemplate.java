package shodrone.core.domain.showproposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.representations.RepresentationBuilder;
import eapli.framework.representations.Representationable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import shodrone.sp_plugin.DocumentValidator;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Embeddable
@Entity
public class ShowProposalTemplate implements AggregateRoot<Long>, Representationable {

    @Id
    @GeneratedValue
    Long id;


    @XmlElement
    @JsonProperty
    @Lob
    private Blob showProposalTemplate;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private Calendar additionDate;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private String filePath;


    public ShowProposalTemplate() {
    }

    public ShowProposalTemplate(File template, Calendar additionDate, Path filePath){
        validate(template);

        this.showProposalTemplate = fileToBlob(template);
        if (additionDate.after(Calendar.getInstance())) {
            throw new IllegalArgumentException("Addition date cannot be in the future");
        }
        this.additionDate= (Calendar) additionDate.clone();
        defineFilePath(filePath);
    }


    private Blob fileToBlob(File template){
        Blob blob;
        try {
            byte[] fileContent = Files.readAllBytes(template.toPath());
            blob = new SerialBlob(fileContent);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating blob from file");
        }

        return blob;
    }

    public File blobToFile(String name, String path) {
        Preconditions.noneNull(this.showProposalTemplate, path, name);
        File file;
        try{
            file = new File(path + name + ".txt");
            if (!file.exists()) {
                boolean created = file.createNewFile();

                if (!created) {
                    throw new IllegalArgumentException("Error creating file");
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] blobContent = this.showProposalTemplate.getBytes(1, (int) this.showProposalTemplate.length());
            fos.write(blobContent);
            fos.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating file from blob");
        }
        return file;
    }

    private void defineFilePath(Path filePath){
        String fullPathStr = filePath.toAbsolutePath().toString();
        String keyword = "shodrone.sp_plugin";
        int startIndex = fullPathStr.indexOf(keyword);
        if (startIndex != -1) {
            String relativePath = fullPathStr.substring(startIndex);
            this.filePath = relativePath;
        } else {
            this.filePath = fullPathStr;
        }
    }

    public static boolean validate(File template){
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("[Customer Representative Name]", "Alice Smith");
        substitutions.put("[Company name]", "DroneOps Inc.");
        substitutions.put("[Company Name]", "DroneOps Inc.");
        substitutions.put("[Address with postal code and country]",
                "123 Drone Lane, 4400-001 Vila Nova de Gaia, Portugal");
        substitutions.put("[VAT Number]", "PT123456789");
        substitutions.put("[proposal number]", "2025-DRONE-007");
        substitutions.put("[show proposal number]", "2025-DRONE-007");
        substitutions.put("[date]", "June 7, 2025"); // Current date
        substitutions.put("[date of the event]", "July 15, 2025");
        substitutions.put("[time of the event]", "14:00 WEST");
        substitutions.put("[GPS coordinates of the location]", "41.1399° N, 8.6186° W");
        substitutions.put("[duration]", "60");
        substitutions.put("[model]", "DroneX;DroneY;DroneZ");
        substitutions.put("[quantity]", "5;10;7");
        substitutions.put("[position in show]", "1;2;3");
        substitutions.put("[figure name]", "Spiral Ascend;Spiral Descend;Looping Spiral");
        substitutions.put("[page break]", "\f"); // ASCII form feed for page break
        substitutions.put("[CRM Manager Name]", "Bob Johnson");
        substitutions.put("[link to show video]", "https://example.com/sim-video-7");
        substitutions.put("[link to show's simulation video]", "https://example.com/sim-video-7");
        substitutions.put("[insurance amount]", "1,000,000 EUR");
        substitutions.put("[valor do seguro]", "1,000,000 EUR");
        substitutions.put("<EOF>", "");
        return DocumentValidator.validatePreGeneration(template,substitutions);
    }

    @Override
    public boolean sameAs(Object other) {
        return false;
    }

    @Override
    public Long identity() {
        return 0L;
    }

    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }
}
