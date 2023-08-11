package se.umu.cs.dv21cgn.radioinfo.api.sr;

import se.umu.cs.dv21cgn.radioinfo.api.RadioApiProgramResponse;

/**
 * A record representing a response from the SR (Sveriges Radio) API when requesting program information.
 * Implements the RadioApiProgramResponse interface to provide a method for converting the API response into a Program object.
 */
public record SrRadioApiProgramResponse(
        int id,
        String name,
        String description,
        String programimage
) implements RadioApiProgramResponse {

}
