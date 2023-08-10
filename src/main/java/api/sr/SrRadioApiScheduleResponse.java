package api.sr;

import api.RadioApiScheduleResponse;
import model.Program;
import model.Schedule;
import java.util.Collection;

/**
 * A record representing a response from the SR (Sveriges Radio) API when requesting schedule information.
 * Implements the RadioApiScheduleResponse interface to provide a method for converting the API response into a Schedule object.
 */
public record SrRadioApiScheduleResponse(Collection<SrRadioApiScheduleItemResponse> schedule) implements RadioApiScheduleResponse {

    /**
     * {@inheritDoc}
     */
    public Schedule toSchedule() {
        Schedule schedule = new Schedule();
        for (SrRadioApiScheduleItemResponse itemResponse : this.schedule) {
            Program program = itemResponse.toProgram();
            schedule.add(program);
        }
        return schedule;
    }
}
