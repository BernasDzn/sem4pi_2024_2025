package shodrone.core.application.drone_model;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.domain.drone_model.DroneModelBuilder;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.drone_model.DroneModelRepository;

public class AddDroneModelController {

	private final AuthorizationService authz = AuthzRegistry.authorizationService();
	private final DroneModelRepository repo = PersistenceContext.repositories().droneModels();

	public DroneModel addDroneModel(String modelName, String droneDSL) {
		authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER);
		final DroneModel newDroneModel = new DroneModelBuilder().named(modelName)
				.withDSL("1", droneDSL)
				.build();
		return repo.save(newDroneModel);
	}


	
}
