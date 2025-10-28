package shodrone.core.domain.showproposal;

import java.util.Map.Entry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eapli.framework.domain.model.DomainEntity;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import shodrone.core.domain.figure.Figure;

@Entity
public class ShowProposalFigures implements DomainEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShowProposalFigureEntry> figures;

    protected ShowProposalFigures() {
    }

    public ShowProposalFigures(final Map<Integer, Figure> figuresMap) {
        validate(figuresMap);
        this.figures = new LinkedList<>();
        for (Entry<Integer, Figure> entry : figuresMap.entrySet()) {
            this.figures.add(new ShowProposalFigureEntry(entry.getValue(), entry.getKey()));
        }
    }

    public static void validate(Map<Integer, Figure> figuresMap) {
        Preconditions.nonNull(figuresMap, "Figures map cannot be null");
        if (figuresMap.isEmpty()) {
            throw new IllegalArgumentException("Figures map cannot be empty");
        }
        
        Integer previousOrder = null;
        Figure previousFigure = null;
        for (Entry<Integer, Figure> entry : figuresMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("Figure cannot be null");
            }
            if (entry.getKey() <= 0) {
                throw new IllegalArgumentException("Order number must be greater than zero");
            }

            if (previousOrder != null){
                if (entry.getKey() <= previousOrder) 
                    throw new IllegalArgumentException("Order numbers must be unique and in ascending order");

                if (entry.getKey() - previousOrder > 1)
                    throw new IllegalArgumentException("Order numbers must be consecutive");
            }
            else{
                if (entry.getKey() != 1)
                    throw new IllegalArgumentException("First order number must be 1");
            }

            previousOrder = entry.getKey();

            if (previousFigure != null && previousFigure.equals(entry.getValue())) {
                throw new IllegalArgumentException("Can't have same figure consecutively in the proposal");
            }
            previousFigure = entry.getValue();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ShowProposalFigureEntry entry : figures) {
            sb.append(entry.toString())
              .append("\n");
        }
        return sb.toString();
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof ShowProposalFigures that)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        return this.figures.equals(that.figures);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShowProposalFigures)) return false;
        ShowProposalFigures that = (ShowProposalFigures) o;
        return identity().equals(that.identity()) && figures.equals(that.figures);
    }
}
