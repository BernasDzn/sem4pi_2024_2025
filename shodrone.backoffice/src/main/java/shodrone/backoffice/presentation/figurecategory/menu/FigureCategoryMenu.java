package shodrone.backoffice.presentation.figurecategory.menu;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.MenuOptionsAndLabels;
import shodrone.backoffice.presentation.figurecategory.*;

public class FigureCategoryMenu implements MenuOptionsAndLabels{
    // FigureCategory
    private static final int REGISTER_FIGURE_CATEGORY_OPTION = 1;
    private static final int LIST_FIGURE_CATEGORY_OPTION = 2;
    private static final int EDIT_FIGURE_CATEGORY_OPTION = 3;
    private static final int ACTIVATE_DEACTIVATE_FIGURE_CATEGORY_OPTION = 4;

    // Edit menu
    private static final int EDIT_NAME_OPTION = 1;
    private static final int EDIT_DESCRIPTION_OPTION = 2;

    // Activate/Deactivate menu
    private static final int ACTIVATE_FIGURE_CATEGORY_OPTION = 1;
    private static final int DEACTIVATE_FIGURE_CATEGORY_OPTION = 2;

    public static Menu build() {
        final var menu = new Menu("Figure Category >");

        menu.addItem(REGISTER_FIGURE_CATEGORY_OPTION, "Register Figure Category", new RegisterFigureCategoryAction());
        menu.addItem(LIST_FIGURE_CATEGORY_OPTION, "List Figure Categories", new ListFigureCategoryAction());
        final Menu editFigureMenu = buildEditFigureMenu();
        menu.addSubMenu(EDIT_FIGURE_CATEGORY_OPTION, editFigureMenu);
        final Menu activateFigureMenu = buildActivateFigureMenu();
        menu.addSubMenu(ACTIVATE_DEACTIVATE_FIGURE_CATEGORY_OPTION, activateFigureMenu);
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private static Menu buildEditFigureMenu() {
        final var menu = new Menu("Edit Figure Category");
        menu.addItem(EDIT_NAME_OPTION, "Edit Name", new EditFigureCategoryNameAction());
        menu.addItem(EDIT_DESCRIPTION_OPTION, "Edit Description", new EditFigureCategoryDescriptionAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    public static Menu buildActivateFigureMenu() {
        final var menu = new Menu("Activate/Deactivate Figure Category");
        menu.addItem(ACTIVATE_FIGURE_CATEGORY_OPTION, "Activate Figure Category", new ActivateFigureCategoryAction());
        menu.addItem(DEACTIVATE_FIGURE_CATEGORY_OPTION, "Deactivate Figure Category", new DeactivateFigureCategoryAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }
}