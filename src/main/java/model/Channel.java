package model;

/**
 * A record representing a radio channel.
 */
public record Channel(
        int id,
        String name,
        Schedule schedule
) { }
