package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class EditShowRequestPlaceAction implements Action {
    @Override
    public boolean execute(){
        return new EditShowRequestPlaceUI().show();
    }
}
