package shodrone.core.domain.showproposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.representations.RepresentationBuilder;
import eapli.framework.representations.Representationable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.showrequest.ShowRequest;

import java.util.*;

@XmlRootElement
@Entity
public class ShowProposal implements AggregateRoot<Long>, Representationable {

    /**
     * The id of the show proposal
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement
    @JsonProperty
    @Getter
    @ManyToOne(optional = false)
    private ShowRequest showRequest;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ShowProposalStatus status;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private ShowProposalDescription description;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private ShowProposalDuration duration;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private ShowProposalNumberOfDrones numberOfDrones;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private Date date;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private Place place;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private ShowProposalVideo video;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ShowProposalFigures figures;

    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShowProposalDroneModelTypes> droneModelTypes = new ArrayList<>();

    public ShowProposal() {
    }

    public ShowProposal(ShowRequest showRequest, ShowProposalStatus status, ShowProposalDescription description,
                          ShowProposalDuration duration, ShowProposalNumberOfDrones numberOfDrones,
                        ShowProposalFigures figures, List<ShowProposalDroneModelTypes> droneModelTypes, ShowProposalVideo video, Place place, Date date) {
        Preconditions.noneNull(showRequest, status, description, duration, numberOfDrones, place, date, figures);
        this.showRequest = showRequest;
        this.status = status;
        this.description = description;
        this.duration = duration;
    
        this.figures = figures;
        this.droneModelTypes.addAll(droneModelTypes);

        this.numberOfDrones = numberOfDrones;
        this.video = video;
        this.place = place;
        this.date = date;
    }

    @Override
    public boolean sameAs(Object other) {
        if (!(other instanceof ShowProposal that)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        return identity().equals(that.identity())
                && this.numberOfDrones.equals(that.numberOfDrones)
                && this.duration == that.duration
                && this.description.equals(that.description)
                && this.place.equals(that.place)
                && this.date.equals(that.date)
                && this.status.equals(that.status);
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }

    public boolean isAccpeted() {
        return this.status == ShowProposalStatus.APPROVED;
    }

    public String toJson() {
        Map<String, String> map = new HashMap<>();
        map.put("showProposalId", this.id.toString());
        map.put("numberOfDrones", this.numberOfDrones.toString());
        map.put("duration", this.duration.toString());
        map.put("place", this.place.toString());
        map.put("date", this.date.toString());
        map.put("video", this.video.toString());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing ShowProposal to JSON", e);
    }
    }

    public Map<String, String> getSubstitutions(){
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("[Customer Representative Name]", getShowRequest().getCustomer().getRepresentatives().iterator().next().getRepresentativeName().toString());
        substitutions.put("[Company name]", getShowRequest().getCustomer().toString());
        substitutions.put("[Company Name]", getShowRequest().getCustomer().toString());
        substitutions.put("[Address with postal code and country]",
                getShowRequest().getCustomer().getAddress().toString());
        substitutions.put("[VAT Number]", getShowRequest().getCustomer().getVatNumber().toString());
        substitutions.put("[proposal number]", identity().toString());
        substitutions.put("[show proposal number]", identity().toString());
        Calendar current= Calendar.getInstance();
        substitutions.put("[date]",String.format("%02d/%02d/%04d %02d:%02d", current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.MONTH) + 1, current.get(Calendar.YEAR), current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE)) );
        String[] parts = getShowRequest().getDate().toString().split(" ");
        substitutions.put("[date of the event]",parts[0]);
        substitutions.put("[time of the event]", parts[1]);
        substitutions.put("[GPS coordinates of the location]", getShowRequest().getPlace().toString());
        substitutions.put("[duration]", getDuration().toString());

        List<ShowProposalDroneModelTypes> droneModels = getDroneModelTypes();
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (ShowProposalDroneModelTypes model : droneModels) {
            countMap.put(model.getDroneModel().toString(), countMap.getOrDefault(model.getDroneModel().toString(), 0) + 1);
        }
        List<String> models  = new ArrayList<>(countMap.keySet());
        List<String> quantities = new ArrayList<>();
        for (String model : models) {
            quantities.add(String.valueOf(countMap.get(model)));
        }
        String modelStr = String.join(";", models);
        String quantityStr = String.join(";", quantities);

        substitutions.put("[model]", modelStr);
        substitutions.put("[quantity]", quantityStr);
        List<String> figureNames  = new ArrayList<>();
        List<String> positions = new ArrayList<>();
        List<ShowProposalFigureEntry> figures = getFigures().getFigures();
        for (ShowProposalFigureEntry figure : figures) {
            figureNames.add(figure.getFigure().getFigureName().toString());
            positions.add(String.valueOf(figure.getOrderNumber()));
        }
        String figuresStr = String.join(";", figureNames);
        String positionsStr = String.join(";", positions);
        substitutions.put("[position in show]", positionsStr);
        substitutions.put("[figure name]", figuresStr);
        substitutions.put("[page break]", "\f"); // ASCII form feed for page break
        substitutions.put("[CRM Manager Name]", getShowRequest().getAuthor().username().toString());
        substitutions.put("[link to show video]", getVideo().getVideoUrl());
        substitutions.put("[link to show's simulation video]", getVideo().getVideoUrl());
        substitutions.put("[insurance amount]", "1,000,000 EUR");
        substitutions.put("[valor do seguro]", "1,000,000 EUR");
        substitutions.put("<EOF>", "");
        return substitutions;
    }

    @Override
    public String toString() {
        return "Show Proposal " + id + " at " + date;        
    }

}
