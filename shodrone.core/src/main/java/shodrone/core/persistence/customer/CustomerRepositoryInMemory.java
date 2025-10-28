package shodrone.core.persistence.customer;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer.CustomerStatus;
import shodrone.core.domain.customer.VATNumber;
import shodrone.core.domain.customer_rep.Representative;

import java.util.List;
import java.util.Optional;


public class CustomerRepositoryInMemory
        extends InMemoryDomainRepository<Customer, VATNumber>
        implements CustomerRepository {


    @Override
    public List<Customer> findAllByStatus(CustomerStatus status) {
        return matchOne(e -> e.getStatus().equals(status)).stream().toList();
    }

    @Override
    public Iterable<Customer> customers() {
        return findAll();
    }

    @Override
    public Iterable<Customer> findAllActive() {
        return match(e -> e.getStatus().isActive());
    }

    @Override
    public Iterable<Representative> findRepresentativesOf(Customer selectedCustomer) {
        Iterable<Customer> customer = match(e -> e.getVatNumber().equals(selectedCustomer.getVatNumber()));
        return customer.iterator().next().getRepresentatives();
    }

    @Override
    public Optional<Customer> findByRepresentative(Representative representative) {
        return matchOne(e -> e.getRepresentatives().contains(representative));
    }

}
