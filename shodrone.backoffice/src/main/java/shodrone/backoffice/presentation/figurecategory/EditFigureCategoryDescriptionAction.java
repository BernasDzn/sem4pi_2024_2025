package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.actions.Action;

public class EditFigureCategoryDescriptionAction implements Action {
    @Override
    public boolean execute() {
        return new EditFigureCategoryDescriptionUI().show();
    }
}
