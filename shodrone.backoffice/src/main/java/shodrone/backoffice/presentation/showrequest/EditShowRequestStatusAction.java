package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class EditShowRequestStatusAction implements Action {
    @Override
    public boolean execute() {
        return new EditShowRequestStatusUI().show();
    }
}
