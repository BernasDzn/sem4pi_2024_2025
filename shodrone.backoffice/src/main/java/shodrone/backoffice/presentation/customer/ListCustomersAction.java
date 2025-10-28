package shodrone.backoffice.presentation.customer;

import eapli.framework.actions.Action;

public class ListCustomersAction implements Action {
    @Override
    public boolean execute() {return new ListCustomerUI().show();}
}
