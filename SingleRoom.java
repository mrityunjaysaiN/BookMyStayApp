/**
 * Represents a single room type in the hotel.
 *
 * A single room is typically designed for one guest and includes one bed.
 * It inherits shared room behavior from the Room abstract class.
 *
 * @author Book My Stay App
 * @version 2.0
 */
public class SingleRoom extends Room {

    public SingleRoom() {
        super("Single", 1, 150, 79.99);
    }
}
