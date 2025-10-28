package shodrone.core.domain.figurecategory;


import com.fasterxml.jackson.annotation.JsonProperty;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.representations.RepresentationBuilder;
import eapli.framework.representations.Representationable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;


@XmlRootElement
@Entity
public class FigureCategory implements AggregateRoot<Designation>, Representationable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    @Column(unique = true, nullable = false)
    private Designation name;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private Description description;

    @Getter
    @XmlElement
    @JsonProperty
    private Calendar creationDate;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private Calendar lastUpdateDate;

    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private boolean active;

    /**
     * Constructor.
     *
     * @param name            the name of the figure category
     * @param description     the description of the figure category
     * @param creationDate    the creation date of the figure category
     * @param lastUpdateDate  the last update date of the figure category
     */
    public FigureCategory(final Designation name, final Description description, final Calendar creationDate, final Calendar lastUpdateDate) {
        Preconditions.noneNull(name, description, creationDate, lastUpdateDate);
        this.name = name;
        this.description = description;
        this.creationDate = (Calendar) creationDate.clone();
        this.lastUpdateDate = (Calendar) lastUpdateDate.clone();
        this.active = true;
    }

    public FigureCategory() {
        this.name = null;
        this.description = null;
        this.creationDate = Calendar.getInstance();
        this.lastUpdateDate = Calendar.getInstance();
        this.active = false;
        // for ORM
    }

    public void updateDate() {
        this.lastUpdateDate = Calendar.getInstance();
    }

    @Override
    public Designation identity() {
        return this.name;
    }

    @Override
    public boolean sameAs(final Object other) {
        if (!(other instanceof FigureCategory that)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        return identity().equals(that.identity())
                && this.description.equals(that.description)
                && this.creationDate.equals(that.creationDate)
                && this.lastUpdateDate.equals(that.lastUpdateDate);
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof FigureCategory that)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        return identity().equals(that.identity())
                && this.description.equals(that.description)
                && this.creationDate.equals(that.creationDate)
                && this.lastUpdateDate.equals(that.lastUpdateDate);
    }

    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }
}
