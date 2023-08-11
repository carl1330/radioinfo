package se.umu.cs.dv21cgn.radioinfo.model;

import java.time.LocalDateTime;

/**
 * A record representing a radio program.
 */
public record Program(
        int id,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String icon
) {
}
