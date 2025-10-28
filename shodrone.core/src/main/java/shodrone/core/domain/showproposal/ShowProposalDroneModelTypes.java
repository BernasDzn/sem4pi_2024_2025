package shodrone.core.domain.showproposal;

import java.util.List;

import eapli.framework.domain.model.DomainEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.domain.figure.Figure;

@Entity
public class ShowProposalDroneModelTypes implements DomainEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    private DroneModel droneModel;

    @Getter
    @ManyToOne
    private Figure figure;

    @Getter
    private List<String> droneTypes;

    public ShowProposalDroneModelTypes(DroneModel droneModel, Figure figure, List<String> droneTypes) {
        validate(droneModel, figure, droneTypes);
        this.droneModel = droneModel;
        this.figure = figure;
        this.droneTypes = droneTypes;
    }

    protected ShowProposalDroneModelTypes() {
    }

    public static void validate(DroneModel droneModel, Figure figure, List<String> droneTypes) {
        if (droneModel == null)
            throw new IllegalArgumentException("The drone type " + droneTypes + " need to have a drone model!");

        if (figure == null)
            throw new IllegalArgumentException("The drone type " + droneTypes + " is not associated to a valid figure!");

        if (droneTypes == null || droneTypes.isEmpty())
            throw new IllegalArgumentException(
                    "A drone model " + droneModel.getDroneModelName() + " need to have a drone type!");

        List<String> allFigureTypes = figure.getDSLCode().getDrone_types();
        for(String type : droneTypes){
            if (!allFigureTypes.contains(type)) {
                throw new IllegalArgumentException("The drone type " + type +" is not part of the figure " + figure.getFigureName());
            }
        }
        
    }

    @Override
    public String toString() {
        return droneModel.getDroneModelName() + " - " + figure.getFigureName() + " - " + droneTypes;
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
        if (!(other instanceof ShowProposalDroneModelTypes)) {
            return false;
        }
        ShowProposalDroneModelTypes that = (ShowProposalDroneModelTypes) other;
        return this.droneModel.equals(that.droneModel) && this.figure.equals(that.figure)
                && this.droneTypes.equals(that.droneTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShowProposalDroneModelTypes)) return false;
        ShowProposalDroneModelTypes that = (ShowProposalDroneModelTypes) o;
        return identity().equals(that.identity()) &&
               droneModel.equals(that.droneModel) &&
               figure.equals(that.figure) &&
               droneTypes.equals(that.droneTypes);
    }
}
