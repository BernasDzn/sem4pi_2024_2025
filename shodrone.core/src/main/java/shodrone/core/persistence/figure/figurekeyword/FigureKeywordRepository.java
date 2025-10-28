package shodrone.core.persistence.figure.figurekeyword;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;

import java.util.Optional;


/**
 * @author Jorge Santos ajs@isep.ipp.pt 02/04/2016
 */
public interface FigureKeywordRepository extends DomainRepository<String, FigureKeyword> {
    Iterable<FigureKeyword> keywords();

    Optional<FigureKeyword> findByName(String keyword);

}
