package shodrone.core.application.customer_rep;

import java.util.Set;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.application.user.AddUserController;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.domain.customer_rep.RepresentativeStatus;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.customer_rep.RepresentativeRepository;

public class RegisterRepresentativeController {

        private final AuthorizationService authz = AuthzRegistry.authorizationService();
        private final RepresentativeRepository repo = PersistenceContext.repositories().representatives();
        private final AddUserController addUserController = new AddUserController();

        public Representative registerCustomerRepresentative(
                String repEmail, String repPosition,
                String repFistName, String repLastName,
                String repUsername, String repPassword
        ){

                String repName = repFistName + " " + repLastName;

                SystemUser repUser = addUserController.addUser(repUsername, repPassword, repFistName
                ,repLastName, repEmail, Set.of(ShodroneRoles.CUSTOMER));
                
                Representative newRepresentative = new Representative(
                        repName, repEmail, repPosition, RepresentativeStatus.ACTIVE, repUser
                );

        
            authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
            return repo.save(newRepresentative);
        }
        
}
