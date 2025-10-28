package shodrone.backoffice.presentation.showproposal;

import eapli.framework.actions.Action;

public class ChangeShowProposalTemplateAction implements Action {

    @Override
    public boolean execute() {
        return new ChangeShowProposalTemplateUI().show();
    }
}
