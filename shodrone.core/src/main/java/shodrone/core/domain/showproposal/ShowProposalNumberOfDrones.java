package shodrone.core.domain.showproposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlElement;

@Embeddable
public class ShowProposalNumberOfDrones implements ValueObject {

    @XmlElement
    @JsonProperty
    private int numberOfDrones;

    public ShowProposalNumberOfDrones() {
    }

    public ShowProposalNumberOfDrones(int numberOfDrones) {
        validate(numberOfDrones);
        this.numberOfDrones = numberOfDrones;
    }

    public static void validate(int numberOfDrones) {
        Preconditions.nonNull(numberOfDrones);
        if (numberOfDrones < 1) {
            throw new IllegalArgumentException("Number of drones must be greater than 0");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(numberOfDrones);
    }

}