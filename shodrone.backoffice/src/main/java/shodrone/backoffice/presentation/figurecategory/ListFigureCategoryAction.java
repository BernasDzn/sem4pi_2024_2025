package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.actions.Action;

public class ListFigureCategoryAction implements Action {
    @Override
    public boolean execute() {
        return new ListFigureCategoryUI().show();
    }
}
