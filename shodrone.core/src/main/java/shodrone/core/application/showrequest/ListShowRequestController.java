package shodrone.core.application.showrequest;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.showrequest.ShowRequestRepository;

import java.util.ArrayList;

public class ListShowRequestController {
    private final ShowRequestRepository showRequestRepository = PersistenceContext
            .repositories().showRequests();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    public Iterable<ShowRequest> allShowRequests() {
        return showRequestRepository.findAllShowRequests();
    }

    public Iterable<ShowRequest> allShowRequestsByCustomer(Customer customer) {
        return showRequestRepository.findShowRequestsByCustomer(customer);
    }

    public Iterable<ShowRequest> allShowRequestsByAuthor() {

        if (authz.session().isEmpty())
            return new ArrayList<>();

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
            return allShowRequests();
        }

        return showRequestRepository.findShowRequestsByAuthor(authz.session().get().authenticatedUser());
    }

    public Iterable<ShowRequest> allShowRequestsByCustomerAndAuthor(Customer customer) {

        if (authz.session().isEmpty())
            return new ArrayList<>();

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
            return allShowRequestsByCustomer(customer);
        }

        return showRequestRepository.findShowRequestsByCustomerAndAuthor(customer, authz.session().get().authenticatedUser());
    }
}
