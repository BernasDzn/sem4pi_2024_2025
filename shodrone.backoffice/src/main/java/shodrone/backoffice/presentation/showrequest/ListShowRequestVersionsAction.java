package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class ListShowRequestVersionsAction implements Action {
    @Override
    public boolean execute() {
        return new ListShowRequestVersionsUI().show();
    }
}
