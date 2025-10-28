package shodrone.backoffice.presentation.figure.menu;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import shodrone.backoffice.presentation.MenuOptionsAndLabels;
import shodrone.backoffice.presentation.figure.*;

public class FigureMenu implements MenuOptionsAndLabels{
    // FigureCategory
    private static final int ADD_FIGURE_OPTION = 1;
    private static final int SEARCH_FIGURE_BY_CATEGORY_OPTION = 1;
    private static final int LIST_PUBLIC_FIGURES_OPTION = 2;
    private static final int SEARCH_FIGURE_BY_KEYWORD_OPTION = 2;
    private static final int DECOMISSION_FIGURE_OPTION = 3;
    private static final int SEARCH_FIGURE_OPTION = 4;

    public static Menu build() {
        final var menu = new Menu("Figure>");

        menu.addItem(ADD_FIGURE_OPTION, "Add Figure", new AddFigureAction());
        menu.addItem(LIST_PUBLIC_FIGURES_OPTION, "List Figures", new ListFigureAction());
        menu.addItem(DECOMISSION_FIGURE_OPTION, "Decomission Figure", new DecomissionFigureAction());
        final Menu searchFigureMenu = buildSearchFigureMenu();
        menu.addSubMenu(SEARCH_FIGURE_OPTION, searchFigureMenu);
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private static Menu buildSearchFigureMenu() {
        final var menu = new Menu("Search Figure");
        menu.addItem(SEARCH_FIGURE_BY_CATEGORY_OPTION, "Search by Category", new SearchFiguresByCategoryAction());
        menu.addItem(SEARCH_FIGURE_BY_KEYWORD_OPTION, "Search by Keyword", new SearchFiguresByKeywordAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }


}