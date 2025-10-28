package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.actions.Action;

public class RegisterFigureCategoryAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterFigureCategoryUI().show();
    }
}
