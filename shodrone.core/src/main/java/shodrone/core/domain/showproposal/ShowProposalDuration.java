package shodrone.core.domain.showproposal;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShowProposalDuration implements ValueObject {

    private int duration;


    public ShowProposalDuration() {
    }


    public ShowProposalDuration(final int duration) {
        validate(duration);
        this.duration = duration;
    }

    public static void validate(int duration) {
        Preconditions.nonNull(duration);
        if (duration < 1) {
            throw new IllegalArgumentException("Duration must be greater than 0 minute");
        }
    }

    @Override
    public String toString() {
        return duration + "m";
    }
}
