package shodrone.backoffice.presentation.showproposal;

import eapli.framework.actions.Action;

public class SendCustomerAction implements Action {

    @Override
    public boolean execute() {
        return new SendCustomerUI().show();
    }
}
