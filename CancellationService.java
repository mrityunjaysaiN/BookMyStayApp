/**
 * Handles booking cancellations and performs controlled rollback operations.
 *
 * This service uses a stack to track released room IDs and ensures
 * inventory and booking history are updated consistently.
 *
 * @author Book My Stay App
 * @version 10.0
 */
import java.util.Stack;

public class CancellationService {

    private final RoomInventory inventory;
    private final BookingHistory bookingHistory;
    private final RoomAllocationService allocationService;
    private final Stack<String> releasedRoomIds;

    public CancellationService(RoomInventory inventory, BookingHistory bookingHistory, RoomAllocationService allocationService) {
        if (inventory == null || bookingHistory == null || allocationService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
        this.allocationService = allocationService;
        this.releasedRoomIds = new Stack<>();
    }

    public boolean cancelReservation(Reservation reservation) throws InvalidBookingException, InvalidInventoryException {
        if (reservation == null) {
            throw new InvalidBookingException("Reservation cannot be null");
        }

        // Check if reservation exists in history
        if (!bookingHistory.hasReservation(reservation)) {
            throw new InvalidBookingException("Reservation not found: " + reservation.getReservationId());
        }

        // Check if already cancelled
        if (bookingHistory.isCancelled(reservation)) {
            throw new InvalidBookingException("Reservation already cancelled: " + reservation.getReservationId());
        }

        String roomType = reservation.getRoomType();
        String roomId = allocationService.getAllocatedRoomId(reservation);

        if (roomId == null) {
            throw new InvalidBookingException("No allocated room found for reservation: " + reservation.getReservationId());
        }

        // Perform rollback operations
        releasedRoomIds.push(roomId);
        inventory.incrementAvailability(roomType);
        bookingHistory.markAsCancelled(reservation);

        return true;
    }

    public Stack<String> getReleasedRoomIds() {
        return (Stack<String>) releasedRoomIds.clone();
    }

    public int getCancellationCount() {
        return releasedRoomIds.size();
    }
}
