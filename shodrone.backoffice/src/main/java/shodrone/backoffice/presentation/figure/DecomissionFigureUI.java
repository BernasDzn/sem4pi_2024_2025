package shodrone.backoffice.presentation.figure;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.application.figure.DecomissionFigureController;
import shodrone.core.domain.figure.Figure;
import shodrone.core.presentation.console.ConsoleEvent;

public class DecomissionFigureUI extends AbstractUI {

    private final DecomissionFigureController controller = new DecomissionFigureController();

    @Override
    protected boolean doShow() {
        final Iterable<Figure> allComissionedFigures = this.controller.allComissionedFigures();
        if (!allComissionedFigures.iterator().hasNext()) {
            System.out.println("There are no registered active Figure");
        } else {
            final SelectWidget<Figure> selector = new SelectWidget<>("Figures:", allComissionedFigures,
                    new FigurePrinter());
            selector.show();
            final Figure selectedFigure = selector.selectedElement();
            controller.decomissionFigure(selectedFigure);
            ConsoleEvent.success("Figure Comissioned: " + selectedFigure.getFigureName());
        }
        return true;
    }

    @Override
    public String headline() {
        return "Decomission Figure";
    }
}
