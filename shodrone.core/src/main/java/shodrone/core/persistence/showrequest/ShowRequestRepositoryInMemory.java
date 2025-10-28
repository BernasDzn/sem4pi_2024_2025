package shodrone.core.persistence.showrequest;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.ShowRequest;

import java.util.Optional;

public class ShowRequestRepositoryInMemory extends InMemoryDomainRepository<ShowRequest, Long> implements ShowRequestRepository {

    @Override
    public Iterable<ShowRequest> findAllShowRequests() {
        return findAll();
    }

    @Override
    public Iterable<ShowRequest> findShowRequestsByCustomer(Customer c) {
        return match(e -> e.getCustomer().equals(c));
    }

    @Override
    public Iterable<ShowRequest> findShowRequestsByAuthor(SystemUser author) {
        return match(e -> e.getAuthor().equals(author));
    }

    @Override
    public Iterable<ShowRequest> findShowRequestsByCustomerAndAuthor(Customer customer, SystemUser author) {
        return match(e -> e.getCustomer().equals(customer) && e.getAuthor().equals(author));
    }

    @Override
    public Optional<ShowRequest> findShowRequestsByDateAndPlace(Place place, Date date) {
        return matchOne(e -> e.getDate().toString().equals(date.toString()) && e.getPlace().equals(place));
    }
}
