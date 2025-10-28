package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import org.hibernate.exception.ConstraintViolationException;
import shodrone.core.application.figurecategory.EditFigureCategoryController;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.presentation.console.ConsoleEvent;

public class EditFigureCategoryNameUI extends AbstractUI {
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
            System.out.println("Current name: " + selectedFigureCategory.getName());
            try {
                String name;
                Designation designation = null;
                do {
                    name = Console.readLine("Figure Category Name");
                    try {
                        designation = Designation.valueOf(name);
                    } catch (Exception e) {
                        ConsoleEvent.error("Name should not be null or have a leading space.");
                        name = "";

                        continue;
                    }
                    try {
                        if (this.controller.isFigureCategoryRegistered(designation)) {
                            throw new Exception("Figure Category already registered");
                        }
                    } catch (Exception e) {
                        ConsoleEvent.error("That name is already in use.");
                        name = "";
                    }
                } while (name.isEmpty());
                this.controller.editFigureCategoryName(selectedFigureCategory, designation);
            } catch (final ConcurrencyException ex) {
                System.out.println(
                        "WARNING: It is not possible to change the figure category state because it was changed by another user");
            } catch (final IntegrityViolationException | ConstraintViolationException ex) {
                System.out.println(
                        "Unfortunately there was an unexpected error in the application. Please try again and if the problem persists, contact your system administrator.");
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "Edit Figure Name Description";
    }
}
