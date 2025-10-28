package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.general.domain.model.Description;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.application.figurecategory.EditFigureCategoryController;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.presentation.console.ConsoleEvent;

public class EditFigureCategoryDescriptionUI extends AbstractUI {

    private final EditFigureCategoryController controller = new EditFigureCategoryController();

    @Override
    protected boolean doShow() {
        final Iterable<FigureCategory> allFigureCategories = this.controller.allFigureCategories();
        if (!allFigureCategories.iterator().hasNext()) {
            System.out.println("There are no registered Figure Categories");
        } else {
            final SelectWidget<FigureCategory> selector = new SelectWidget<>("Figure Categories:", allFigureCategories,
                    new FigureCategoryPrinter());
            selector.show();
            final FigureCategory selectedFigureCategory = selector.selectedElement();
            System.out.println("Current description: " + selectedFigureCategory.getDescription());
            try {
                String desc;
                Description description = null;
                do {
                    desc = Console.readLine("Figure Category Description");
                    try {
                        description = Description.valueOf(desc);
                    } catch (Exception e) {
                        desc = "";
                        ConsoleEvent.error("Description should not be null.");
                    }
                } while (desc.isEmpty());
                this.controller.editFigureCategoryDescription(selectedFigureCategory, description);
            } catch (final ConcurrencyException ex) {
                System.out.println(
                        "WARNING: It is not possible to change the figure category state because it was changed by another user");
            } catch (final IntegrityViolationException ex) {
                System.out.println(
                        "Unfortunately there was an unexpected error in the application. Please try again and if the problem persists, contact your system administrator.");
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "Edit Figure Category Description";
    }
}
