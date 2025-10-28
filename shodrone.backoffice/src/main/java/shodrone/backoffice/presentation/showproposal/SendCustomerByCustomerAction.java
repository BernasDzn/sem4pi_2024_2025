package shodrone.backoffice.presentation.showproposal;

import eapli.framework.actions.Action;

public class SendCustomerByCustomerAction implements Action {

    @Override
    public boolean execute() {
        return new SendCustomerByCustomerUI().show();
    }
}
