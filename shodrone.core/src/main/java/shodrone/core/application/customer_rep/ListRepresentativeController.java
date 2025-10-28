package shodrone.core.application.customer_rep;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lombok.Getter;
import lombok.Setter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.customer.CustomerRepository;

public class ListRepresentativeController {

	private final AuthorizationService authz = AuthzRegistry.authorizationService();
	private final CustomerRepository customerRepository = PersistenceContext.repositories().customers();
	private final ListCustomerController listCustomerController = new ListCustomerController();

	@Getter
	@Setter
	private Customer selectedCustomer;

    public Iterable<Customer> allCustomers() {
        return listCustomerController.allCustomers();
    }

	public Iterable<Representative> representativesOfSelectedCustomer() {
		authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.POWER_USER);
		return customerRepository.findRepresentativesOf(selectedCustomer);
	}
	
}
