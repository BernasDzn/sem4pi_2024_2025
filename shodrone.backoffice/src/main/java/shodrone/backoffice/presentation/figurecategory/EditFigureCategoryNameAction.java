package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.actions.Action;

public class EditFigureCategoryNameAction implements Action {
    @Override
    public boolean execute() {
        return new EditFigureCategoryNameUI().show();
    }
}