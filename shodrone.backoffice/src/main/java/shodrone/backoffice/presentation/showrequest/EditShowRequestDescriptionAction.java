package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class EditShowRequestDescriptionAction implements Action {
    @Override
    public boolean execute() {
        return new EditShowRequestDescriptionUI().show();
    }
}
