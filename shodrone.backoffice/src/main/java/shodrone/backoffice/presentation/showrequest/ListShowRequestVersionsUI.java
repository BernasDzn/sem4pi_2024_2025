package shodrone.backoffice.presentation.showrequest;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.showrequest.ShowRequestVersion;

import java.util.ArrayList;

public class ListShowRequestVersionsUI extends AbstractListUI<ShowRequestVersion> {

    @Override
    protected Iterable<ShowRequestVersion> elements() {
        FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
        selectionMenu.withFilter(false);
        ShowRequest selectedShowRequest = selectionMenu.selectShow();
        if (selectedShowRequest == null) {
            return new ArrayList<>();
        }
        return selectedShowRequest.getShowRequestVersions();
    }

    @Override
    protected Visitor<ShowRequestVersion> elementPrinter() {
        return new ShowRequestVersionsPrinter();
    }

    @Override
    protected String elementName() {
        return "";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-20s%-20s%-10s%-10s%-20s%-20s", "CREATION DATE" ,"NUMBER OF DRONES", "DURATION", "PLACE", "DATE", "STATUS");
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    public String headline() {
        return "List Show Request Versions";
    }

}
