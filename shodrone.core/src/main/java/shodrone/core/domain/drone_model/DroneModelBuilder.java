package shodrone.core.domain.drone_model;

import eapli.framework.domain.model.DomainFactory;
import eapli.framework.general.domain.model.Designation;
import shodrone.core.domain.common.DSL;

public class DroneModelBuilder implements DomainFactory<DroneModel> {

	private DroneModel droneModel;

	private Designation droneModelName;
	private DSL droneModelDSL;

	public DroneModelBuilder named(String droneModelName) {
		return named(Designation.valueOf(droneModelName));
	}

	public DroneModelBuilder named(Designation droneModelName) {
		this.droneModelName = droneModelName;
		return this;
	}

	public DroneModelBuilder withDSL(String droneModelVersion, String droneModelSyntax) {
		return withDSL(DSL.valueOf(droneModelVersion, droneModelSyntax));	
	}

	public DroneModelBuilder withDSL(DSL droneModelDSL) {
		this.droneModelDSL = droneModelDSL;
		return this;
	}

	private DroneModel buildOrThrow(){
		if (droneModel != null) {
			return droneModel;
		}else{
			droneModel = new DroneModel(droneModelName, droneModelDSL);
			return droneModel;
		}
	}

	@Override
	public DroneModel build() {
		final DroneModel newDroneModel = buildOrThrow();
		this.droneModel = null; // Reset the builder after building
		return newDroneModel;
	}
}
