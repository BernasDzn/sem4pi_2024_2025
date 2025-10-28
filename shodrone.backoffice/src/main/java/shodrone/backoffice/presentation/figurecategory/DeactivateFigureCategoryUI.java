package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.application.figurecategory.DeactivateFigureCategoryController;
import shodrone.core.domain.figurecategory.FigureCategory;

public class DeactivateFigureCategoryUI extends AbstractUI {

    private final DeactivateFigureCategoryController controller = new DeactivateFigureCategoryController();

    @Override
    protected boolean doShow() {
        final Iterable<FigureCategory> allFigureCategories = this.controller.allActiveFigureCategories();
        if (!allFigureCategories.iterator().hasNext()) {
            System.out.println("There are no registered active Figure Categories");
        } else {
            final SelectWidget<FigureCategory> selector = new SelectWidget<>("Figure Categories:", allFigureCategories, new FigureCategoryPrinter());
            selector.show();
            final FigureCategory selectedFigureCategory = selector.selectedElement();
            controller.deactivateFigureCategory(selectedFigureCategory);
        }
        return true;
    }

    @Override
    public String headline() {
        return "Activate Figure Category";
    }
}
