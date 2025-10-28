package shodrone.backoffice.presentation.showproposal;

import eapli.framework.actions.Action;

public class RegisterShowProposalAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterShowProposalUI().show();
    }
}
