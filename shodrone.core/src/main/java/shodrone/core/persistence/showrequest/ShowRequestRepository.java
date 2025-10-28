package shodrone.core.persistence.showrequest;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.ShowRequest;
import java.util.Optional;

public interface ShowRequestRepository extends DomainRepository<Long, ShowRequest> {
    Iterable<ShowRequest> findAllShowRequests();

    Iterable<ShowRequest> findShowRequestsByCustomer(Customer c);

    Iterable<ShowRequest> findShowRequestsByAuthor(SystemUser author);

    Iterable<ShowRequest> findShowRequestsByCustomerAndAuthor(Customer customer, SystemUser author);

    Optional<ShowRequest> findShowRequestsByDateAndPlace(Place place, Date date);
}