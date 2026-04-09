/**
 * Maintains a historical record of confirmed bookings.
 *
 * This class stores confirmed reservations in the order they were processed,
 * providing an audit trail for administrative review and reporting.
 *
 * @author Book My Stay App
 * @version 8.0
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingHistory {

    private final List<Reservation> confirmedBookings;

    public BookingHistory() {
        this.confirmedBookings = new ArrayList<>();
    }

    public void addConfirmedBooking(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(confirmedBookings);
    }

    public int getTotalBookings() {
        return confirmedBookings.size();
    }

    public List<Reservation> getBookingsByRoomType(String roomType) {
        if (roomType == null || roomType.isBlank()) {
            throw new IllegalArgumentException("Room type must not be empty");
        }
        return confirmedBookings.stream()
                .filter(reservation -> roomType.equals(reservation.getRoomType()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("BookingHistory[totalBookings=%d]", confirmedBookings.size());
    }
}
