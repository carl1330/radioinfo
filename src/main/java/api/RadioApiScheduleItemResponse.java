package api;

import model.Program;

/**
 * An interface representing a response item from a radio API when requesting schedule information.
 * Implementing classes should provide a method to convert the API response item into a Program object.
 */
public interface RadioApiScheduleItemResponse {

    /**
     * Converts the API response item into a Program object.
     *
     * @return A Program object representing the program information from the API response item.
     */
    Program toProgram();
}
