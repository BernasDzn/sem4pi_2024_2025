package shodrone.core.application.figure;

import shodrone.core.domain.figure.Figure;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figure.FigureRepository;


public class ListFigureController {

    private final FigureRepository figureRepository = PersistenceContext
            .repositories().figures();

    public Iterable<Figure> allComissionedFigures() {
        return figureRepository.findAllComissioned();
    }
}
