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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingHistory {

    private final List<Reservation> confirmedBookings;
    private final Set<String> cancelledReservationIds;

    public BookingHistory() {
        this.confirmedBookings = new ArrayList<>();
        this.cancelledReservationIds = new HashSet<>();
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

    public boolean hasReservation(Reservation reservation) {
        if (reservation == null) {
            return false;
        }
        return confirmedBookings.stream()
                .anyMatch(r -> r.getReservationId().equals(reservation.getReservationId()));
    }

    public boolean isCancelled(Reservation reservation) {
        if (reservation == null) {
            return false;
        }
        return cancelledReservationIds.contains(reservation.getReservationId());
    }

    public void markAsCancelled(Reservation reservation) {
        if (reservation != null) {
            cancelledReservationIds.add(reservation.getReservationId());
        }
    }

    public int getCancelledCount() {
        return cancelledReservationIds.size();
    }

    @Override
    public String toString() {
        return String.format("BookingHistory[totalBookings=%d, cancelled=%d]", confirmedBookings.size(), cancelledReservationIds.size());
    }
}
