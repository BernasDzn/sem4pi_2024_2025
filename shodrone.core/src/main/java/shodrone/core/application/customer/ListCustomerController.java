package shodrone.core.application.customer;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.customer.CustomerRepository;

public class ListCustomerController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final CustomerRepository customerRepository = PersistenceContext.repositories().customers();

    public Iterable<Customer> allCustomers() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.POWER_USER);
        return customerRepository.findAll();
    }
}
