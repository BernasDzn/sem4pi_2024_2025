package shodrone.core.persistence.customer;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer.CustomerStatus;
import shodrone.core.domain.customer.VATNumber;
import shodrone.core.domain.customer_rep.Representative;

import java.util.Optional;

public interface CustomerRepository
        extends DomainRepository<VATNumber, Customer> {

    default Optional<Customer> findByVATNumber(final VATNumber vatNumber) {
        return ofIdentity(vatNumber);
    };

    Iterable<Customer> findAllByStatus(CustomerStatus status);

    Iterable<Customer> customers();

    Iterable<Customer> findAllActive();

	Iterable<Representative> findRepresentativesOf(Customer selectedCustomer);

    Optional<Customer> findByRepresentative(Representative representative);
}
