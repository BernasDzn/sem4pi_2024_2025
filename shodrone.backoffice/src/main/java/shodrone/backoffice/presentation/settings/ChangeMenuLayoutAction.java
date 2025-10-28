package shodrone.backoffice.presentation.settings;

import eapli.framework.actions.Action;

public class ChangeMenuLayoutAction implements Action {
    @Override
    public boolean execute() {
        return new ChangeMenuLayoutUI().show();
    }
}
