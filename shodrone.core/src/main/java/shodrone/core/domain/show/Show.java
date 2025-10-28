package shodrone.core.domain.show;

import shodrone.core.domain.common.Date;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import eapli.framework.domain.model.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import shodrone.core.domain.showproposal.ShowProposal;

/**
 * Represents a Show in the system.
 * <br><br>
 * A Show is a finalized version of a ShowProposal.
 */
@Entity
public class Show implements AggregateRoot<Long> {

	/**
     * ORM primary key. This is an implementation detail and is never exposed to the outside of the
     * class.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

	/**
	 * The ShowProposal that this Show is based on.
	 * <br><br>
	 * This is a OneToOne relation, meaning that each Show can only be based on one ShowProposal.
	 * <br><br>
	 * If the ShowProposal is accepted, it will be converted to a Show.
	 */
	@Getter
	@OneToOne
	private ShowProposal showProposal;

	/**
	 * Empty constructor for ORM.
	 */
	protected Show(){ this.showProposal = null;}

	/**
	 * Constructor to create a Show from a ShowProposal.
	 * <br><br>
	 * The relation is OneToOne (if accepted)
	 * @param showProposal
	 */
	public Show(ShowProposal showProposal) {
		this.showProposal = showProposal;
	}

	public Date getShowDate() {
		return showProposal.getDate();
	}

	public boolean inTheFuture() {
		return getShowDate().getDate().after(new java.util.Date());
	}

	@Override
	public boolean sameAs(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Show)) {
			return false;
		}
		Show that = (Show) other;
		return this.pk != null && this.pk.equals(that.pk);
	}

	@Override
	public Long identity() {
		return this.pk;
	}

	public String toJson() {
        Map<String, String> map = new HashMap<>();
        map.put("showId", this.pk.toString());
        map.put("date", this.showProposal.getDate().toString());
		map.put("numberOfDrones", this.showProposal.getNumberOfDrones().toString());
		map.put("duration", this.showProposal.getDuration().toString());
		map.put("place", this.showProposal.getPlace().toString());
		map.put("video", this.showProposal.getVideo().getVideoUrl().toString());
		map.put("figures", this.showProposal.getFigures().toString());
		map.put("droneModelTypes", this.showProposal.getDroneModelTypes().toString());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing ShowProposal to JSON", e);
    	}
    }
	
}
