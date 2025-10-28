package shodrone.core.persistence.show;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.show.Show;

public class ShowRepositoryInMemory
	extends InMemoryDomainRepository<Show, Long>
    implements ShowRepository {

	@Override
	public Iterable<Show> findAllShows() {
		return findAll();
	}

	@Override
	public Iterable<Show> findAllShowsByCustomerId(Customer customer) {
		return match(e -> e.getShowProposal().getShowRequest().getCustomer()
				.getVatNumber().equals(customer.getVatNumber()));
	}

	@Override
	public Iterable<Show> findAllScheduledShowsByCustomerId(Customer customer) {
		return match(e -> e.getShowProposal().getShowRequest().getCustomer()
				.getVatNumber().equals(customer.getVatNumber()) && e.inTheFuture());
	}
	
}
