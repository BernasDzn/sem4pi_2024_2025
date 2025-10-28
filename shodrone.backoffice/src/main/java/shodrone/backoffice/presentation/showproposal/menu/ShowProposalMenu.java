package shodrone.backoffice.presentation.showproposal.menu;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.MenuOptionsAndLabels;
import shodrone.backoffice.presentation.showproposal.*;

public class ShowProposalMenu implements MenuOptionsAndLabels {

    private static final int REGISTER_SHOW_PROPOSAL_OPTION = 1;
    private static final int SEARCH_CUSTOMER_BY_CUSTOMER_OPTION = 1;
    private static final int ADD_SHOW_PROPOSAL_TEMPLATE = 1;
    private static final int SEND_CUSTOMER_SHOW_PROPOSAL_OPTION = 2;
    private static final int SEARCH_CUSTOMER_BY_SHOW_PROPOSAL_OPTION = 2;
    private static final int CHANGE_SHOW_PROPOSAL_TEMPLATE = 2;
    private static final int CONFIGURE_SHOW_PROPOSAL_TEMPLATE_OPTION = 3;
    private static final int SCHEDULE_ACCEPTED_SHOW_PROPOSAL_OPTION = 4;


    public static Menu build() {
        final var menu = new Menu("Show Proposal >");

        menu.addItem(REGISTER_SHOW_PROPOSAL_OPTION, "Register Show Proposal", new RegisterShowProposalAction());
        final Menu sendCustomerMenu = buildSendShowProposalMenu();
        menu.addSubMenu(SEND_CUSTOMER_SHOW_PROPOSAL_OPTION, sendCustomerMenu);
        final Menu configureShowProposalTemplateMenu = buildConfigureShowProposalTemplateMenu();
        menu.addSubMenu(CONFIGURE_SHOW_PROPOSAL_TEMPLATE_OPTION, configureShowProposalTemplateMenu);
        menu.addItem(SCHEDULE_ACCEPTED_SHOW_PROPOSAL_OPTION, "Schedule Accepted Show Proposal", new ScheduleAcceptedShowProposalAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private static Menu buildSendShowProposalMenu() {
        final var menu = new Menu("Send Show Proposal to Customer");
        menu.addItem(SEARCH_CUSTOMER_BY_CUSTOMER_OPTION, "Filter by Customer's Proposals", new SendCustomerByCustomerAction());
        menu.addItem(SEARCH_CUSTOMER_BY_SHOW_PROPOSAL_OPTION, "All Show Proposals", new SendCustomerAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private static Menu buildConfigureShowProposalTemplateMenu() {
        final var menu = new Menu("Configure Show Proposal Template");
        menu.addItem(ADD_SHOW_PROPOSAL_TEMPLATE, "Add Show Proposal Template", new AddShowProposalTemplateAction());
        menu.addItem(CHANGE_SHOW_PROPOSAL_TEMPLATE, "Change Show Proposal Template", new ChangeShowProposalTemplateAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

}
