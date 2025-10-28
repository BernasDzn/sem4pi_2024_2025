package shodrone.backoffice.presentation.figure;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.application.figure.SearchFiguresController;
import shodrone.core.application.figurecategory.ListFigureCategoryController;
import shodrone.core.domain.figure.Figure;
import shodrone.backoffice.presentation.figurecategory.FigureCategoryPrinter;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.presentation.console.ConsoleEvent;


public class SearchFiguresByCategoryUI extends AbstractUI {

    private final SearchFiguresController controller = new SearchFiguresController();
    private final ListFigureCategoryController categoryController = new ListFigureCategoryController();


    @Override
    protected boolean doShow() {
        FigureCategory selectedCategory = null;
        while (selectedCategory==null) {
            try {
                final Iterable<FigureCategory> allCategories = categoryController.allFigureCategories();

                if (allCategories.iterator().hasNext()) {
                    final SelectWidget<FigureCategory> selector = new SelectWidget<>("Select a category:", allCategories, new FigureCategoryPrinter());
                    selector.show();
                    selectedCategory = selector.selectedElement();
                    Iterable<Figure> foundFigures= controller.allSpecificCategoryFigures(selectedCategory);

                    System.out.printf("#  %-10s%-10s%-20s%-40s\n", "NAME", "CATEGORY", "VERSION", "DESCRIPTION");
                    final FigurePrinter printer= new FigurePrinter();
                    for(Figure found:foundFigures){
                        printer.visit(found);
                        System.out.println("");
                    }
                } else {
                    System.out.println("No Figure Categories available to be selected.");
                    return false;
                }
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
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
