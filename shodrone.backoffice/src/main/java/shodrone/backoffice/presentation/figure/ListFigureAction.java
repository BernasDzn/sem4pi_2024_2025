package shodrone.backoffice.presentation.figure;

import eapli.framework.actions.Action;

public class ListFigureAction implements Action {
    @Override
    public boolean execute() {
        return new ListFigureUI().show();
    }
}
