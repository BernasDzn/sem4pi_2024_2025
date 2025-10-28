package shodrone.backoffice.presentation.customer_rep.menu;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.MenuOptionsAndLabels;
import shodrone.backoffice.presentation.customer_rep.ListRepresentativeAction;
import shodrone.backoffice.presentation.customer_rep.RegisterRepresentativeAction;

public class RepresentativesMenu implements MenuOptionsAndLabels {

    // CUSTOMER
    private static final int REGISTER_REPRESENTATIVE_OPTION = 1;
    private static final int LIST_REPRESENTATIVE_OPTION = 2;

    public static Menu build() {
        final var menu = new Menu("Customer Representatives >");

        menu.addItem(REGISTER_REPRESENTATIVE_OPTION, "Register Customer Representative",
            new RegisterRepresentativeAction());
        menu.addItem(LIST_REPRESENTATIVE_OPTION, "List Customer Representatives", 
            new ListRepresentativeAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

}
