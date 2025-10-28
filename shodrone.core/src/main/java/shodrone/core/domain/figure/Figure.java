package shodrone.core.domain.figure;


import com.fasterxml.jackson.annotation.JsonProperty;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.representations.RepresentationBuilder;
import eapli.framework.representations.Representationable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.dsl_plugin.DSLProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@XmlRootElement
@Entity
public class Figure implements AggregateRoot<FigureName>, Representationable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    @AttributeOverride(name = "name", column = @Column(unique = true, nullable = false))
    private FigureName figureName;

    /**
     * Version of the figure.
     */
    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private VersionNumber versionNumber;

    /**
     * Description of the figure.
     */
    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private Description description;

    /**
     * Status of the figure.
     * To see all possible statuses, see {@link FigureStatus}
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private FigureStatus status;

    /**
     * Customer that requested figure.
     * Many to One relationship since a Customer can have multiple figures.
     */
    @Getter
    @Setter
    @ManyToOne
    @XmlElement
    @JsonProperty
    private Customer exclusiveTo;

    /**
     * Code of the figure.
     */
    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    @Embedded
    private FigureDSL DSLCode;

    /**
     * Category of the figure.
     */
    @Getter
    @Setter
    @ManyToOne
    @XmlElement
    @JsonProperty
    private FigureCategory category;

    /**
     * Show Designer responsible for figure.
     * Many to One relationship since a Show Designer can design multiple figures.
     */
    @Getter
    @Setter
    @ManyToOne
    @XmlElement
    @JsonProperty
    private SystemUser showDesigner;


    /**
     * Tags assigned to figure.
     */
    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    @OneToMany
    private List<FigureKeyword> keywords;

    /**
     * Constructor for the figure.
     * @param figureName
     * @param description
     * @param versionNumber
     * @param DSLCode
     * @param status
     * @param exclusiveTo
     * @param category
     * @param showDesigner
     */
    public Figure(final FigureName figureName, final Description description, final VersionNumber versionNumber,
                  final File DSLCode, final FigureStatus status,
                  final Customer exclusiveTo, final FigureCategory category, final SystemUser showDesigner, final List<FigureKeyword> keywords) {
      Preconditions.noneNull(figureName, description, versionNumber, status, category, showDesigner, keywords, DSLCode);
        this.figureName=figureName;
        this.description= description;
        this.versionNumber=versionNumber;
        this.DSLCode= createFigureDSL(DSLCode);
        if( this.DSLCode == null) {
            throw new IllegalArgumentException("DSL Code could not be read from the provided file.");
        }
        this.status = status;
        this.exclusiveTo=exclusiveTo;
        this.category = category;
        this.showDesigner=showDesigner;
        this.keywords= keywords;
    }

    protected Figure() {

    }


    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }

    @Override
    public boolean sameAs(Object other) {
        if(this==other){
            return true;
        }
        if(other==null || getClass()!=other.getClass()){
            return false;
        }
        Figure that=(Figure) other;
        return this.figureName.equals(that.figureName);
    }

    @Override
    public FigureName identity() {
        return this.figureName;
    }

    private FigureDSL createFigureDSL(File DSLFile){
        try{
            DSLProcessor processor = new DSLProcessor();
            String DSLCode = new String(Files.readAllBytes(DSLFile.toPath()));
            String DSLVersion = processor.extractDSLVersion(DSLFile);
            List<String> drones = processor.extractDroneTypes(DSLFile);
            return new FigureDSL(DSLCode, DSLVersion, drones);
        } catch (IOException e) {
            System.out.println("Error reading DSL file: " + e.getMessage());
            return null;
        }

    }

}
