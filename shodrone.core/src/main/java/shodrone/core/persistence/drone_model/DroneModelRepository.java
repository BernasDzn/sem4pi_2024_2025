package shodrone.core.persistence.drone_model;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.drone_model.DroneModel;

public interface DroneModelRepository 
	extends DomainRepository<Long, DroneModel> {

	/**
	 * Finds a DroneModel by its name.
	 *
	 * @param droneModelName the name of the drone model
	 * @return the DroneModel if found, otherwise null
	 */
	DroneModel findByDroneModelName(String droneModelName);

	/**
	 * Retrieves all DroneModels.
	 *
	 * @return a list of all DroneModels
	 */
	Iterable<DroneModel> droneModels();
	
}
