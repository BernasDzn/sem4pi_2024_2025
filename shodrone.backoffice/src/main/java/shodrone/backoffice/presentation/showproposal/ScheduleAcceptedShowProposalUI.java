package shodrone.backoffice.presentation.showproposal;

import java.util.ArrayList;
import java.util.List;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.domain.show.Show;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalStatus;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.show.ShowRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepository;

public class ScheduleAcceptedShowProposalUI extends AbstractUI{

	@Override
	protected boolean doShow() {
		try{
			ShowProposalRepository prop_rep = PersistenceContext.repositories().showProposals();
			Iterable<ShowProposal> showProposals = prop_rep.findAllShowProposals();
			ShowRepository show_rep = PersistenceContext.repositories().shows();
			List<ShowProposal> acceptedShowProposals = new ArrayList<>();
			for (ShowProposal showProposal : showProposals) {
				if (showProposal.getStatus() == ShowProposalStatus.APPROVED) {
					acceptedShowProposals.add(showProposal);
				}
			}
			SelectWidget<ShowProposal> selector = new SelectWidget<>("Select a show proposal to schedule:", acceptedShowProposals);
			selector.show();
			if(selector.selectedOption() != 0){
				System.out.println("Scheduling show proposal with id " + selector.selectedElement().identity() + " to date " + selector.selectedElement().getDate());
				Show newShow = new Show(selector.selectedElement());
				show_rep.save(newShow);
			}

		}catch (Exception e) {
			System.out.println("Error retrieving show proposals: " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public String headline() {
		return "Schedule Accepted Show Proposal";
	}

}
