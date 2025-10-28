package shodrone.backoffice.presentation.figure;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.backoffice.presentation.figurecategory.FigureCategoryPrinter;
import shodrone.core.application.figure.SearchFiguresController;
import shodrone.core.application.figurecategory.ListFigureCategoryController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.presentation.console.ConsoleEvent;


public class SearchFiguresByKeywordUI extends AbstractUI {

    private final SearchFiguresController controller = new SearchFiguresController();
    private final ListFigureCategoryController categoryController = new ListFigureCategoryController();


    @Override
    protected boolean doShow() {
        String keyword;
        keyword = Console.readNonEmptyLine("Insert Keyword", "cannot be empty");
        Iterable<Figure> foundFigures= controller.allFiguresWithKeyword(keyword);

        if(foundFigures.iterator().hasNext()){
            System.out.printf("#  %-10s%-10s%-20s%-40s\n", "NAME", "CATEGORY", "VERSION", "DESCRIPTION");
            final FigurePrinter printer= new FigurePrinter();
            for(Figure found:foundFigures){
                printer.visit(found);
            }
        }else{
            System.out.println("No Figure found.");
            return false;
        }
    return true;
    }

    @Override
    public String headline() {
        return "Search Figure";
    }


    protected String emptyMessage() {
        return "No data.";
    }
}
