package shodrone.backoffice.presentation.customer.menu;

import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.MenuOptionsAndLabels;
import shodrone.backoffice.presentation.customer.ListCustomersAction;
import shodrone.backoffice.presentation.customer.RegisterCustomerAction;

public class CustomersMenu implements MenuOptionsAndLabels {

    // CUSTOMER
    private static final int REGISTER_CUSTOMER_OPTION = 1;
    private static final int LIST_CUSTOMERS_OPTION = 2;

    public static Menu build() {
        final var menu = new Menu("Customer >");

        menu.addItem(REGISTER_CUSTOMER_OPTION, "Register Customer", new RegisterCustomerAction());
        menu.addItem(LIST_CUSTOMERS_OPTION, "List all Customers", new ListCustomersAction());
        //menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

}
