package shodrone.backoffice.presentation.figure;

import eapli.framework.actions.Action;


public class DecomissionFigureAction implements Action {
    @Override
    public boolean execute() {
        return new DecomissionFigureUI().show();
    }
}
