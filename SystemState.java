/**
 * Represents the complete system state for persistence.
 *
 * This class encapsulates all the critical state that needs to be preserved
 * across application restarts, including inventory and booking history.
 *
 * @author Book My Stay App
 * @version 12.0
 */
import java.io.Serializable;

public class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final RoomInventory inventory;
    private final BookingHistory bookingHistory;

    public SystemState(RoomInventory inventory, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public RoomInventory getInventory() {
        return inventory;
    }

    public BookingHistory getBookingHistory() {
        return bookingHistory;
    }

    @Override
    public String toString() {
        return String.format("SystemState[inventory=%s, bookingHistory=%s]",
                           inventory, bookingHistory);
    }
}