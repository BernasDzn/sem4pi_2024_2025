package shodrone.core.domain.showrequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;

import java.util.Calendar;

@Entity
public class ShowRequestVersion {

    @XmlElement
    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement
    @JsonProperty
    @Getter
    private ShowRequestNumberOfDrones numberOfDrones;

    @XmlElement
    @JsonProperty
    @Getter
    private ShowRequestDuration duration;

    @XmlElement
    @JsonProperty
    @Getter
    private ShowRequestDescription description;

    @XmlElement
    @JsonProperty
    @Getter
    private Place place;

    @XmlElement
    @JsonProperty
    @Getter
    private Date date;

    @XmlElement
    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Getter
    private ShowRequestStatus status;

    @XmlElement
    @JsonProperty
    @Getter
    private Calendar creationDate;

    public ShowRequestVersion(final ShowRequestNumberOfDrones numberOfDrones, final ShowRequestDuration duration,
                              final ShowRequestDescription description, final Place place,
                              final Date date, final ShowRequestStatus status) {
        Preconditions.noneNull(numberOfDrones, duration, description, place, date, status);
        this.numberOfDrones = numberOfDrones;
        this.duration = duration;
        this.description = description;
        this.place = place;
        this.date = date;
        this.status = status;
        this.creationDate = Calendar.getInstance();
    }

    public ShowRequestVersion(){

    }

    @Override
    public String toString() {
        return "creationDate=" + creationDate.getTime()+
                ", numberOfDrones=" + numberOfDrones +
                ", duration=" + duration +
                ", description=" + description +
                ", place=" + place +
                ", date=" + date +
                ", status=" + status;
    }
}
