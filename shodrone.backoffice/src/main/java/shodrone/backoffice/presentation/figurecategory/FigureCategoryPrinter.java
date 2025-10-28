package shodrone.backoffice.presentation.figurecategory;


import eapli.framework.visitor.Visitor;
import shodrone.core.domain.figurecategory.FigureCategory;

public class FigureCategoryPrinter implements Visitor<FigureCategory> {
    @Override
    public void visit(final FigureCategory visitee) {
        System.out.printf("%-10s%-10s%-20s%-40s%-30s", visitee.isActive(), visitee.getName(), visitee.getDescription(), visitee.getCreationDate().getTime(), visitee.getLastUpdateDate().getTime());
    }
}
