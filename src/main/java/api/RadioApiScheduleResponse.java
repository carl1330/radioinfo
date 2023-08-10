package api;

import model.Schedule;

/**
 * An interface representing a response from a radio API when requesting schedule information.
 * Implementing classes should provide a method to convert the API response into a Schedule object.
 */
public interface RadioApiScheduleResponse {

    /**
     * Converts the API response into a Schedule object.
     *
     * @return A Schedule object containing the schedule information from the API response.
     */
    Schedule toSchedule();
}
