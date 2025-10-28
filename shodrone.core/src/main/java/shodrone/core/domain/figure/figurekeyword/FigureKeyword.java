package shodrone.core.domain.figure.figurekeyword;


import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.representations.RepresentationBuilder;
import eapli.framework.representations.Representationable;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;


@XmlRootElement
@Entity
public class FigureKeyword implements AggregateRoot<String>, Representationable {

    @Id
    @Getter
    @Setter
    @XmlElement
    @JsonProperty
    private String keyword;


    public FigureKeyword(String keyword) {
        this.keyword=keyword;
    }

      protected FigureKeyword() {

    }


    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }

    @Override
    public boolean sameAs(Object other) {
        return false;
    }

    @Override
    public String identity() {
        return null;
    }

    public static FigureKeyword valueOf(String keywordString) {
        return new FigureKeyword(keywordString);
    }
}
