package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.actions.Action;

public class DeactivateFigureCategoryAction implements Action {
    @Override
    public boolean execute() {
        return new DeactivateFigureCategoryUI().show();
    }
}
