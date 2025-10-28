package shodrone.backoffice.presentation.figure;

import eapli.framework.actions.Action;

public class AddFigureAction implements Action {

    @Override
    public boolean execute() {
        return new AddFigureUI().show();
    }
}
