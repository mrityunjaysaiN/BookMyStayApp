/**
 * Exception thrown when inventory operations would result in invalid state.
 *
 * This exception is used to prevent negative availability or other
 * inventory constraint violations.
 *
 * @author Book My Stay App
 * @version 9.0
 */
public class InvalidInventoryException extends Exception {

    public InvalidInventoryException(String message) {
        super(message);
    }

    public InvalidInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
