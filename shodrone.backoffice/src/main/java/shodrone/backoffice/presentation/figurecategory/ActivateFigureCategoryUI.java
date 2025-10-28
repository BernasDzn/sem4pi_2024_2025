package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.application.figurecategory.ActivateFigureCategoryController;
import shodrone.core.domain.figurecategory.FigureCategory;

public class ActivateFigureCategoryUI extends AbstractUI {

    private final ActivateFigureCategoryController controller = new ActivateFigureCategoryController();

    @Override
    protected boolean doShow() {
        final Iterable<FigureCategory> allFigureCategories = this.controller.allInactiveFigureCategories();
        if (!allFigureCategories.iterator().hasNext()) {
            System.out.println("There are no registered deactivated Figure Categories");
        } else {
            final SelectWidget<FigureCategory> selector = new SelectWidget<>("Figure Categories:", allFigureCategories, new FigureCategoryPrinter());
            selector.show();
            final FigureCategory selectedFigureCategory = selector.selectedElement();
            controller.activateFigureCategory(selectedFigureCategory);
        }
        return true;
    }

    @Override
    public String headline() {
        return "Activate Figure Category";
    }
}
