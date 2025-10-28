package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class ListShowRequestByCustomerAction implements Action {
    @Override
    public boolean execute() {
        return new ListShowRequestByCustomerUI().show();
    }
}
