package shodrone.core.persistence.showproposal;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalStatus;
import shodrone.core.domain.showrequest.ShowRequest;

import java.util.Optional;

public interface ShowProposalRepository extends DomainRepository<Long, ShowProposal> {
    Iterable<ShowProposal> findAllShowProposals();
    Iterable<ShowProposal> findAllShowProposalsReadyToTest();
    Optional<ShowProposal> findShowProposalsById(Long proposalId);
    Optional<ShowProposal> findByShowRequest(ShowRequest showRequest);
    Iterable<ShowProposal> findAllCustomerProposalsReadyToTest(Customer selectedCustomer);
    Iterable<ShowProposal> findAllCustomerProposals(Customer selectedCustomer);
    
    default public boolean changeToAcceptedWithId(Long proposalId) {
        Optional<ShowProposal> proposal = findShowProposalsById(proposalId);
        if (proposal.isPresent()) {
            ShowProposal showProposal = proposal.get();
            showProposal.setStatus(ShowProposalStatus.APPROVED);
            save(showProposal);
            return true;
        }
        return false;
    }

    default public boolean changeToRejectedWithId(Long proposalId) {
        Optional<ShowProposal> proposal = findShowProposalsById(proposalId);
        if (proposal.isPresent()) {
            ShowProposal showProposal = proposal.get();
            showProposal.setStatus(ShowProposalStatus.REJECTED);
            save(showProposal);
            return true;
        }
        return false;
    }

}