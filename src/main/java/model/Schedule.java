package model;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A class representing a schedule of programs.
 */
public class Schedule {

    private final ArrayList<Program> programs = new ArrayList<>();

    /**
     * Returns a copy of the list of programs in the schedule.
     *
     * @return A copy of the list of programs.
     */
    public synchronized ArrayList<Program> getPrograms() {
        return new ArrayList<>(programs);
    }

    /**
     * Adds a program to the schedule if it passes validation.
     *
     * @param program The program to be added.
     */
    public synchronized void add(Program program) {
        if (validate_program(program)) {
            programs.add(program);
        }
    }

    /**
     * Validates if a program is within a valid time range.
     * The time range in this case is between 12 hours before and 6 hours after the current time.
     *
     * @param program The program to be validated.
     * @return True if the program is within the valid time range, otherwise false.
     */
    private static boolean validate_program(Program program) {
        LocalDateTime twelveHoursBefore = LocalDateTime.now().minusHours(12);
        LocalDateTime sixHoursAfter = LocalDateTime.now().plusHours(6);
        return !program.startTime().isBefore(twelveHoursBefore) && !program.startTime().isAfter(sixHoursAfter);
    }

    /**
     * Checks if the schedule is empty.
     *
     * @return True if the schedule is empty, otherwise false.
     */
    public synchronized boolean isEmpty() {
        return programs.isEmpty();
    }
}
