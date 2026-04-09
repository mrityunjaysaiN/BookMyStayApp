/**
 * Represents a double room type in the hotel.
 *
 * A double room is designed for two guests and includes two beds.
 * It inherits shared room behavior from the Room abstract class.
 *
 * @author Book My Stay App
 * @version 2.0
 */
public class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double", 2, 220, 129.99);
    }
}
