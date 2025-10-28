package shodrone.customerapp.presentation;

import eapli.framework.actions.Action;
import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import shodrone.core.presentation.console.Colorize;
import shodrone.customerapp.CustomerAppCli;

public class ViewShowProposal extends Menu{

	private static final String MENU_TITLE_I = "+=====[ Showing Proposal with ID: ";
	private static final String MENU_TITLE_F = " ]=====+";

	private static final int EXIT_OPTION = 0;

	Integer proposalId;

	public ViewShowProposal(Integer proposalId) {
		super(MENU_TITLE_I + proposalId + MENU_TITLE_F);
		this.proposalId = proposalId;
		buildViewShowProposalMenu();
	}

	private void buildViewShowProposalMenu() {
		String proposal_message = CustomerAppCli.getProposalAsTemplate(proposalId.toString());
		try{
			if (proposal_message == null || proposal_message.isEmpty()) {
				System.out.println(Colorize.toRed("No proposal found with ID: " + proposalId));
				return;
			}
		}catch (Exception e) {
			return;
		}
		addItem(MenuItem.separator(Colorize.toYellow(proposal_message)));
		addItem(1, "Accept Proposal", new AcceptProposalAction(proposalId)::execute);
		addItem(2, "Reject Proposal", new RejectProposalAction(proposalId)::execute);
		addItem(EXIT_OPTION, "Return to Notifications", () -> true);
	}


}

class RejectProposalAction implements Action{

	private final Integer proposalId;

	RejectProposalAction(Integer proposalId) {
		this.proposalId = proposalId;
	}

	@Override
	public boolean execute() {
		try {
			CustomerAppCli.rejectProposal(proposalId.toString());
			System.out.println(Colorize.toGreen("Proposal with ID: " + proposalId + " rejected successfully."));
		} catch (Exception e) {
			System.out.println(Colorize.toRed("Error rejecting proposal with ID: " + proposalId));
		}
		return true;
	}
	
}

class AcceptProposalAction implements Action{

	private final Integer proposalId;

	AcceptProposalAction(Integer proposalId) {
		this.proposalId = proposalId;
	}

	@Override
	public boolean execute() {
		try {
			CustomerAppCli.acceptProposal(proposalId.toString());
			System.out.println(Colorize.toGreen("Proposal with ID: " + proposalId + " accepted successfully."));
		} catch (Exception e) {
			System.out.println(Colorize.toRed("Error accepting proposal with ID: " + proposalId));
		}
		return true;
	}
	
}
