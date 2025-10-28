package shodrone.backoffice.presentation.user;

import eapli.framework.actions.Action;

public class ActivateUserAction implements Action{

    @Override
    public boolean execute() {
        return new ActivateUserUI().show();
    }
    
}
