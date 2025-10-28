package shodrone.backoffice.presentation.figure;

import eapli.framework.actions.Action;


public class SearchFiguresByCategoryAction implements Action {
    @Override
    public boolean execute() {
        return new SearchFiguresByCategoryUI().show();
    }
}
