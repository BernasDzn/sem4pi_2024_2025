package shodrone.backoffice.presentation.showproposal;

import eapli.framework.actions.Action;

public class AddShowProposalTemplateAction implements Action {

    @Override
    public boolean execute() {
        return new AddShowProposalTemplateUI().show();
    }
}
