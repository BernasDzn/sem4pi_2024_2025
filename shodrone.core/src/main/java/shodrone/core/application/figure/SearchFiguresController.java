package shodrone.core.application.figure;

import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figure.FigureRepository;


public class SearchFiguresController {

    private final FigureRepository figureRepository = PersistenceContext
            .repositories().figures();

    public Iterable<Figure> allSpecificCategoryFigures(FigureCategory selectedCategory) {
        return figureRepository.findAllCategoryFigures(selectedCategory);
    }

    public Iterable<Figure> allFiguresWithKeyword(String keyword) {
        return figureRepository.allFiguresWithKeyword(keyword);
    }
}
