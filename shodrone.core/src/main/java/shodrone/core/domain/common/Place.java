package shodrone.core.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlElement;

@Embeddable
public class Place implements ValueObject {

    @XmlElement
    @JsonProperty
    private double latitude;

    @XmlElement
    @JsonProperty
    private double longitude;

    public Place() {
    }

    public Place(double latitude, double longitude) {
        validate(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(String coordinates) {
        Preconditions.nonNull(coordinates);
        validate(coordinates);
        String[] parts = coordinates.split(",");
        this.latitude = Double.parseDouble(parts[0]);
        this.longitude = Double.parseDouble(parts[1]);
    }

    public static void validate(String coordinates) {

        Preconditions.nonNull(coordinates);
        coordinates = coordinates.trim();
        if (!coordinates.matches("^-?\\d*(\\.\\d+)?\\s*,\\s*-?\\d*(\\.\\d+)?$")) {
            throw new IllegalArgumentException("Latitude and Longitude must be valid numbers");
        }
        String[] parts = coordinates.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        validate(lat, lon);
    }

    public static void validate(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }

    @Override
    public String toString() {
        return latitude +", " + longitude;
    }
}
