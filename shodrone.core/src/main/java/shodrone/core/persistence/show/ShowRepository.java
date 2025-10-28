package shodrone.core.persistence.show;

import java.util.Optional;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.show.Show;

public interface ShowRepository extends DomainRepository<Long, Show> {

	default Optional<Show> findById(Long id){
		return ofIdentity(id);
	};
	Iterable<Show> findAllShows();
	Iterable<Show> findAllShowsByCustomerId(Customer customer);
	Iterable<Show> findAllScheduledShowsByCustomerId(Customer customer);
	
}
