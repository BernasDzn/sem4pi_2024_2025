package shodrone.backoffice.presentation.figure;

import eapli.framework.actions.Action;


public class SearchFiguresByKeywordAction implements Action {
    @Override
    public boolean execute() {
        return new SearchFiguresByKeywordUI().show();
    }
}
