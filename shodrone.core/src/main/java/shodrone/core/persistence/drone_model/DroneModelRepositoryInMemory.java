package shodrone.core.persistence.drone_model;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.drone_model.DroneModel;

public class DroneModelRepositoryInMemory
extends InMemoryDomainRepository<DroneModel, Long>
implements DroneModelRepository{

	@Override
	public DroneModel findByDroneModelName(String droneModelName) {
		return matchOne(e -> e.getDroneModelName().toString().equals(droneModelName))
				.orElse(null);
	}

	@Override
	public Iterable<DroneModel> droneModels() {
		return findAll();
	}
	
}
