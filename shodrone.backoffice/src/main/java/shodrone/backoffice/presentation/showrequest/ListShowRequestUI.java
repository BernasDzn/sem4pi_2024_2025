package shodrone.backoffice.presentation.showrequest;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import shodrone.core.application.showrequest.ListShowRequestController;
import shodrone.core.domain.showrequest.ShowRequest;

public class ListShowRequestUI extends AbstractListUI<ShowRequest> {

    private final ListShowRequestController theController = new ListShowRequestController();

    @Override
    public String headline() {
        return "List Show Request";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<ShowRequest> elements() {
        return theController.allShowRequestsByAuthor();
    }

    @Override
    protected Visitor<ShowRequest> elementPrinter() {
        return new ShowRequestPrinter();
    }

    @Override
    protected String elementName() {
        return "";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-20s%-20s%-20s%-10s%-20s%-20s%-20s", "AUTHOR", "CUSTOMER", "NUMBER OF DRONES", "DURATION", "PLACE", "DATE", "STATUS");
    }
}
