package shodrone.backoffice.presentation.showrequest.menu;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.MenuOptionsAndLabels;
import shodrone.backoffice.presentation.showrequest.*;

public class ShowRequestMenu implements MenuOptionsAndLabels {
    private static final int REGISTER_SHOW_REQUEST_OPTION = 1;


    //List Show Request
    private static final int LIST_SHOW_REQUEST_OPTION = 2;

    private static final int LIST_SHOW_REQUEST_ALL_OPTION = 1;
    private static final int LIST_SHOW_REQUEST_CUSTOMER_OPTION = 2;


    // Edit Show Request
    private static final int EDIT_SHOW_REQUEST_OPTION = 3;

    private static final int EDIT_NUMBER_OF_DRONES_OPTION = 1;
    private static final int EDIT_DURATION_OPTION = 2;
    private static final int EDIT_DESCRIPTION_OPTION = 3;
    private static final int EDIT_PLACE_OPTION = 4;
    private static final int EDIT_DATE_OPTION = 5;
    private static final int EDIT_STATUS_OPTION = 6;

    // Other options
    private static final int OTHER_SHOW_REQUEST_OPTION = 4;

    private static final int DOWNLOAD_SHOW_REQUEST_DESCRIPTION_OPTION = 1;
    private static final int SEE_SHOW_REQUEST_VERSIONS_OPTION = 2;


    public static Menu build() {
        final var menu = new Menu("Show Request >");

        menu.addItem(REGISTER_SHOW_REQUEST_OPTION, "Register Show Request", new RegisterShowRequestAction());
        final Menu listShowRequestMenu = buildListShowRequestMenu();
        menu.addSubMenu(LIST_SHOW_REQUEST_OPTION, listShowRequestMenu);
        final Menu editShowRequestMenu = buildEditShowRequestMenu();
        menu.addSubMenu(EDIT_SHOW_REQUEST_OPTION, editShowRequestMenu);
        final Menu otherShowRequestMenu = buildOtherShowRequestMenu();
        menu.addSubMenu(OTHER_SHOW_REQUEST_OPTION, otherShowRequestMenu);

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private static Menu buildListShowRequestMenu() {
        final var menu = new Menu("List Show Request");
        menu.addItem(LIST_SHOW_REQUEST_ALL_OPTION, "List All Show Requests", new ListShowRequestAction());
        menu.addItem(LIST_SHOW_REQUEST_CUSTOMER_OPTION, "List Show Requests of a Customer", new ListShowRequestByCustomerAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private static Menu buildEditShowRequestMenu() {
        final var menu = new Menu("Edit Show Request");
        menu.addItem(EDIT_NUMBER_OF_DRONES_OPTION, "Edit Number of Drones", new EditShowRequestNumberOfDronesAction());
        menu.addItem(EDIT_DURATION_OPTION, "Edit Duration", new EditShowRequestDurationAction());
        menu.addItem(EDIT_DESCRIPTION_OPTION, "Edit Description", new EditShowRequestDescriptionAction());
        menu.addItem(EDIT_PLACE_OPTION, "Edit Place", new EditShowRequestPlaceAction());
        menu.addItem(EDIT_DATE_OPTION, "Edit Date", new EditShowRequestDateAction());
        menu.addItem(EDIT_STATUS_OPTION, "Edit Status", new EditShowRequestStatusAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private static Menu buildOtherShowRequestMenu() {
        final var menu = new Menu("Other Show Request options");
        menu.addItem(DOWNLOAD_SHOW_REQUEST_DESCRIPTION_OPTION, "Download Show Request Description", new DownloadShowRequestDescriptionAction());
        menu.addItem(SEE_SHOW_REQUEST_VERSIONS_OPTION, "See Show Request Versions", new ListShowRequestVersionsAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }
}
