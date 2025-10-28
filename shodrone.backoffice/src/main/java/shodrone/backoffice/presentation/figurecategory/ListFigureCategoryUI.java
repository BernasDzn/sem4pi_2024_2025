package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import shodrone.core.application.figurecategory.ListFigureCategoryController;
import shodrone.core.domain.figurecategory.FigureCategory;

@SuppressWarnings({ "squid:S106" })
public class ListFigureCategoryUI extends AbstractListUI<FigureCategory> {
    private final ListFigureCategoryController theController = new ListFigureCategoryController();

    @Override
    public String headline() {
        return "List Figure Category";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<FigureCategory> elements() {
        return theController.allFigureCategories();
    }

    @Override
    protected Visitor<FigureCategory> elementPrinter() {
        return new FigureCategoryPrinter();
    }

    @Override
    protected String elementName() {
        return "";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-10s%-10s%-20s%-40s%-30s", "ACTIVE", "NAME", "DESCRIPTION", "CREATION DATE", "LAST CHANGED");
    }
}