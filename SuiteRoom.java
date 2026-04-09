/**
 * Represents a suite room type in the hotel.
 *
 * A suite room provides additional space and premium amenities.
 * It inherits shared room behavior from the Room abstract class.
 *
 * @author Book My Stay App
 * @version 2.0
 */
public class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite", 3, 420, 249.99);
    }
}
