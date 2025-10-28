package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class EditShowRequestDateAction implements Action {
    @Override
    public boolean execute() {
        return new EditShowRequestDateUI().show();
    }
}
