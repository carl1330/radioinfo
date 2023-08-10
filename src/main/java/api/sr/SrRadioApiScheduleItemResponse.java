package api.sr;

import api.RadioApiScheduleItemResponse;
import model.Program;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

import static java.lang.Long.parseLong;

/**
 * A record representing a program item response within the schedule response from the SR (Sveriges Radio) API.
 * Implements the RadioApiScheduleItemResponse interface to provide a method for converting the response into a Program object.
 */
public record SrRadioApiScheduleItemResponse(
        int episodeid,
        String title,
        String description,
        String starttimeutc,
        String endtimeutc,
        SrRadioApiScheduleProgramItem program,
        String imageurl
) implements RadioApiScheduleItemResponse {

    /**
     *  {@inheritDoc}
     */
    public Program toProgram() {
        LocalDateTime startTime = convertDateString(starttimeutc);
        LocalDateTime endTime = convertDateString(endtimeutc);
        return new Program(episodeid, title, description, startTime, endTime, Objects.requireNonNullElse(imageurl, "none"));
    }

    /**
     * Converts a date string from the API response into a LocalDateTime object.
     *
     * @param dateString The date string from the API response.
     * @return A LocalDateTime object representing the converted date.
     */
    private LocalDateTime convertDateString(String dateString) {
        long timestamp = parseLong(dateString.substring(6, 19));
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }
}
