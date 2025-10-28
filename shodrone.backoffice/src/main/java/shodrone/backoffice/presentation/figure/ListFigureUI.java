package shodrone.backoffice.presentation.figure;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import shodrone.core.application.figure.ListFigureController;
import shodrone.core.domain.figure.Figure;

@SuppressWarnings({ "squid:S106" })
public class ListFigureUI extends AbstractListUI<Figure> {
    private final ListFigureController theController = new ListFigureController();

    @Override
    public String headline() {
        return "List Public Figures";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<Figure> elements() {
        return theController.allComissionedFigures();
    }

    @Override
    protected Visitor<Figure> elementPrinter() {
        return new FigurePrinter();
    }

    @Override
    protected String elementName() {
        return "";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-10s%-10s%-20s%-40s", "NAME", "CATEGORY", "VERSION", "DESCRIPTION");
    }
}