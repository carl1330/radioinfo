package model;

/**
 * An exception class representing a bad HTTP request error.
 */
public class HttpBadRequestException extends Exception {

    /**
     * Constructs a new HttpBadRequestException with a default error message.
     */
    public HttpBadRequestException() {
        super("Bad HTTP request");
    }
}
