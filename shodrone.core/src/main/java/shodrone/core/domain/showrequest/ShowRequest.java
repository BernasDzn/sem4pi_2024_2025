package shodrone.core.domain.showrequest;

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
import shodrone.core.domain.common.Place;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.customer.Customer;

import java.util.HashSet;
import java.util.Set;

@XmlRootElement
@Entity
public class ShowRequest implements AggregateRoot<Long>, Representationable {

    /**
        * The id of the show request
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The author of the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @ManyToOne(optional = false)
    private SystemUser author;

    /**
     * The customer that made the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @ManyToOne(optional = false)
    private Customer customer;

    /**
     * The number of drones of the show in the show request
     **/
    @XmlElement
    @Getter
    @Setter
    @JsonProperty
    private ShowRequestNumberOfDrones numberOfDrones;

    /**
     * The duration (minutes) of the show in the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private ShowRequestDuration duration;

    /**
     * The description of the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private ShowRequestDescription description;

    /**
     * The place of the show in the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private Place place;

    /**
     * The date of the show in the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private Date date;

    /**
     * The status of the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ShowRequestStatus status;

    /**
     * The versions of the show request
     **/
    @XmlElement
    @JsonProperty
    @Getter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShowRequestVersion> showRequestVersions = new HashSet<>();


    /**
     * Constructor.
     *
     * @param customer the customer that made the show request
     * @param numberOfDrones  the number of drones of the show in the show request
     * @param duration     the duration (minutes) of the show in the show request
     * @param description    the description of the show request
     * @param place  the place of the show in the show request
     * @param date  the date of the show in the show request
     * @param status the status of the show request
     */
    public ShowRequest(final SystemUser author, final Customer customer, final ShowRequestNumberOfDrones numberOfDrones, final ShowRequestDuration duration, final ShowRequestDescription description, final Place place, final Date date, final ShowRequestStatus status) {
        Preconditions.noneNull(author, customer, duration, numberOfDrones, description, place, date, status);
        this.author = author;
        this.customer = customer;
        this.numberOfDrones = numberOfDrones;
        this.duration = duration;
        this.description = description;
        this.place = place;
        this.date = date;
        this.status = status;
        this.showRequestVersions.add(new ShowRequestVersion(numberOfDrones, duration, description, place, date, status));
    }

    protected ShowRequest() {}

    public void updateVersions(final ShowRequest sr) {
        Preconditions.noneNull(sr.getNumberOfDrones(), sr.getDuration(), sr.getDescription(), sr.getPlace(), sr.getDate(), sr.getStatus());
        this.showRequestVersions.add(new ShowRequestVersion(sr.getNumberOfDrones(), sr.getDuration(),
                sr.getDescription(), sr.getPlace(), sr.getDate(), sr.getStatus()));
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof ShowRequest that)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        return identity().equals(that.identity())
                && this.customer.equals(that.customer)
                && this.numberOfDrones.equals(that.numberOfDrones)
                && this.duration == that.duration
                && this.description.equals(that.description)
                && this.place.equals(that.place)
                && this.date.equals(that.date)
                && this.status.equals(that.status);
    }

    @Override
    public Long identity() {
        return null;
    }

    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }
}