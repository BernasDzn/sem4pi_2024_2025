package shodrone.backoffice.presentation.figurecategory;

import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.core.application.figurecategory.AddFigureCategoryController;

import java.util.Calendar;

public class RegisterFigureCategoryUI extends AbstractUI {

    private final AddFigureCategoryController theController = new AddFigureCategoryController();

    @Override
    protected boolean doShow() {

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
                if (this.theController.isFigureCategoryRegistered(designation)) {
                    throw new Exception("Figure Category already registered");
                }
            } catch (Exception e) {
                ConsoleEvent.error("That name is already in use.");
                name = "";
            }
        } while (name.isEmpty());

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

        Calendar creationDate = Calendar.getInstance();
        Calendar lastUpdateDate = Calendar.getInstance();

        try {
            this.theController.addFigureCategory(designation, description, creationDate, lastUpdateDate);
            ConsoleEvent.success("Figure Category Registered: " + designation);
        } catch (final Exception e) {
            ConsoleEvent.error("That name is already in use.");
        }

        return false;
    }

    @Override
    public String headline() {
        return "Register Figure Category";
    }
}
