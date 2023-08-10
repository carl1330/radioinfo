package api.sr;

/**
 * A record representing a program item within the schedule response from the SR (Sveriges Radio) API.
 */
public record SrRadioApiScheduleProgramItem(int id, String name) {
}
