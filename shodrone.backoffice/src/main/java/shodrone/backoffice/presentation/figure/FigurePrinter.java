package shodrone.backoffice.presentation.figure;


import eapli.framework.visitor.Visitor;
import shodrone.core.domain.figure.Figure;

public class FigurePrinter implements Visitor<Figure> {
    @Override
    public void visit(final Figure visitee) {
        System.out.printf("%-10s%-10s%-20s%-40s", visitee.getFigureName(), visitee.getCategory().getName(), visitee.getVersionNumber(), visitee.getDescription());
    }
}
