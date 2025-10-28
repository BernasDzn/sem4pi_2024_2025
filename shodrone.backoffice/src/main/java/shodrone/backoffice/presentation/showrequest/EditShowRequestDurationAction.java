package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class EditShowRequestDurationAction implements Action {
    @Override
    public boolean execute() {
        return new EditShowRequestDurationUI().show();
    }
}
