package shodrone.core.domain.showproposal;

import eapli.framework.domain.model.DomainEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import shodrone.core.domain.figure.Figure;

@Entity
class ShowProposalFigureEntry implements DomainEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    private final Figure figure;

    @Getter
    private final int orderNumber;

    public ShowProposalFigureEntry() {
        this.figure = null;
        this.orderNumber = 0;
    }

    public ShowProposalFigureEntry(final Figure figure, final int orderNumber) {
        validate(figure, orderNumber);
        this.figure = figure;
        this.orderNumber = orderNumber;
    }

    private static void validate(Figure figure, int orderNumber) {
        if (figure == null || orderNumber <= 0) {
            throw new IllegalArgumentException("Invalid figure or order number!");
        }
    }

    @Override
    public String toString() {
        return orderNumber + " - " + figure.getFigureName();
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public boolean sameAs(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ShowProposalFigureEntry)) {
            return false;
        }
        ShowProposalFigureEntry that = (ShowProposalFigureEntry) other;
        return this.orderNumber == that.orderNumber && this.figure.equals(that.figure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShowProposalFigureEntry)) return false;
        ShowProposalFigureEntry that = (ShowProposalFigureEntry) o;
        return identity().equals(that.identity()) &&
               orderNumber == that.orderNumber &&
               figure.equals(that.figure);
    }
    
}
