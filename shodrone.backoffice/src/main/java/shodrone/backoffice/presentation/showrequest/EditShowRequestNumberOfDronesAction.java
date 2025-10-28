package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class EditShowRequestNumberOfDronesAction implements Action {

    @Override
    public boolean execute() {
        return new EditShowRequestNumberOfDronesUI().show();
    }
}
