package shodrone.core.domain.drone_model;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.general.domain.model.Designation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import shodrone.core.domain.common.DSL;

/**
 * Represents a drone model. <br>
 * A DroneModel is defined by its name and its DSL (Domain Specific Language).
 */
@XmlRootElement
@Entity
public class DroneModel implements AggregateRoot<Long> {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@AttributeOverride(name = "value", column = 
		@Column(name = "drone_model_name", nullable = false))
	private Designation droneModelName;

	@Column(nullable = false)
	private DSL droneModelDSL;

	/**
	 * Default constructor for ORM.
	 */
	protected DroneModel(){
		this.droneModelName = null;
		this.droneModelDSL = null;
	}

	/**
	 * Constructor to create a DroneModel with a specific name and DSL.
	 * @param droneModelName the name of the drone model
	 * @param droneModelDSL the DSL, also called "Drone Programming Language"
	 */ 
	protected DroneModel(Designation droneModelName, DSL droneModelDSL) {
		this.droneModelName = droneModelName;
		this.droneModelDSL = droneModelDSL;
	}

	@Override
	public boolean sameAs(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DroneModel)) {
			return false;
		}
		DroneModel that = (DroneModel) other;
		return this.id.equals(that.id) &&
				this.droneModelName.equals(that.droneModelName) &&
				this.droneModelDSL.equals(that.droneModelDSL);
	}

	@Override
	public Long identity() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.droneModelName.toString();
	}

}
