package shodrone.core.persistence.drone_model;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.infrastructure.Application;

public class DroneModelRepositoryJpa
extends JpaAutoTxRepository<DroneModel, Long, Long>
implements DroneModelRepository {

	public DroneModelRepositoryJpa(final TransactionalContext autoTx) {
		super(autoTx, "id");
	}

	public DroneModelRepositoryJpa(final String puname) {
		super(puname, Application.settings().getExtendedPersistenceProperties(), "id");
	}

	@Override
	public DroneModel findByDroneModelName(String droneModelName) {
		return match("e.droneModelName = :droneModelName", "droneModelName", droneModelName)
				.stream()
				.findFirst()
				.orElse(null);
	}

	@Override
	public Iterable<DroneModel> droneModels() {
		return findAll();
	}
	
}
