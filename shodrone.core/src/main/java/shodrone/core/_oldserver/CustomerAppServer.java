package shodrone.core._oldserver;

import java.util.LinkedList;
import java.util.Optional;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.customer.CustomerRepository;
import shodrone.core.persistence.customer_rep.RepresentativeRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepository;

public class CustomerAppServer extends Server{

	public static void main(String[] args) {
		try {
			new CustomerAppServer();
		} catch (Exception e) {
			System.err.println("Error starting CustomerAppServer: " + e.getMessage());
		}
	}

	private final RepresentativeRepository repRepo = PersistenceContext.repositories().representatives();
	private final CustomerRepository customerRepo = PersistenceContext.repositories().customers();
	private final ShowProposalRepository showProposalRepo = PersistenceContext.repositories().showProposals();

	public CustomerAppServer() {
		super();
	}

	public LinkedList<String> getMyProposals(){

		LinkedList<String> proposalsList = new LinkedList<>();
		
		final SystemUser user = AuthzRegistry.authorizationService().session().get().authenticatedUser();
		
		final Optional<Representative> rep = repRepo.findByEmailAddress(user.email());
		final Customer customer = customerRepo.findByRepresentative(rep.get()).get();
		Iterable<ShowProposal> proposals = showProposalRepo.findAllCustomerProposals(customer);
		
		for (ShowProposal proposal : proposals) {
			if (!proposal.isAccpeted()) {
				proposalsList.add(proposal.toJson());
			}
		}
		return proposalsList;
	}
	
}
