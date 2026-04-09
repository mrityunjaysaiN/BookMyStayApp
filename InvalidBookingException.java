/**
 * Exception thrown when a booking request is invalid.
 *
 * This exception is used to signal validation failures in booking inputs
 * or system constraints that prevent a reservation from being processed.
 *
 * @author Book My Stay App
 * @version 9.0
 */
public class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }

    public InvalidBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
