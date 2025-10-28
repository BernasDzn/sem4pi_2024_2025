package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class ListShowRequestAction implements Action {

    @Override
    public boolean execute() {
        return new ListShowRequestUI().show();
    }
}
