package shodrone.core.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;

import java.util.Calendar;

@Embeddable
public class Date implements ValueObject {

    @XmlElement
    @JsonProperty
    @Getter
    private Calendar date;

    public Date() {
    }

    public Date(final Calendar date) {
        validate(date);
        if (date.before(Calendar.getInstance())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        this.date = (Calendar) date.clone();
    }

    public Date(final int dayTime, final int monthTime, final int yearTime) {
        Preconditions.noneNull(dayTime, monthTime, yearTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(yearTime, monthTime-1, dayTime, 0, 0);
        validate(calendar);
        this.date = (Calendar) calendar.clone();
    }

    public Date(final int dayTime, final int monthTime, final int yearTime, final int hourTime, final int minuteTime) {
        Preconditions.noneNull(dayTime, monthTime, yearTime, hourTime, minuteTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(yearTime, monthTime-1, dayTime, hourTime, minuteTime);
        validate(calendar);
        this.date = (Calendar) calendar.clone();
    }

    public Date(String date) {
        validate(date);
        String[] parts = date.split(" ");
        String[] dateParts = parts[0].split("/");
        String[] timeParts = parts[1].split(":");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Calendar months are 0-based
        int year = Integer.parseInt(dateParts[2]);
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month, day, hour, minute);
        this.date = (Calendar) calendar.clone();
    }

    public static void validate(Calendar date) {
        Preconditions.nonNull(date);
        if (date.before(Calendar.getInstance())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
    }

    public static void validate(String date) {
        Preconditions.nonNull(date);
        String[] parts = date.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid date format. Expected format: dd/mm/yyyy hh:mm");
        }

        String[] dateParts = parts[0].split("/");
        if (dateParts.length != 3) {
            throw new IllegalArgumentException("Invalid date format. Expected format: dd/mm/yyyy");
        }

        String[] timeParts = parts[1].split(":");
        if (timeParts.length != 2) {
            throw new IllegalArgumentException("Invalid time format. Expected format: hh:mm");
        }


        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setLenient(false);
            calendar.set(year, month - 1, day, hour, minute);
            calendar.getTime();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date or time.");
        }
        validate(calendar);
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d %02d:%02d", date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
    }

}
