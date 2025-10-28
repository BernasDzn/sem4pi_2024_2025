package shodrone.backoffice.presentation.showproposal;

import eapli.framework.actions.Action;


public class ScheduleAcceptedShowProposalAction implements Action {

	@Override
	public boolean execute() {
		return new ScheduleAcceptedShowProposalUI().show();
	}


	
}
