/**
 * Generates reports and summaries from booking history data.
 *
 * This service provides operational visibility into confirmed bookings
 * without modifying the stored historical data.
 *
 * @author Book My Stay App
 * @version 8.0
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingReportService {

    private final BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        if (bookingHistory == null) {
            throw new IllegalArgumentException("Booking history cannot be null");
        }
        this.bookingHistory = bookingHistory;
    }

    public int getTotalConfirmedBookings() {
        return bookingHistory.getTotalBookings();
    }

    public Map<String, Integer> getBookingsByRoomType() {
        Map<String, Integer> roomTypeCounts = new HashMap<>();
        for (Reservation reservation : bookingHistory.getAllBookings()) {
            roomTypeCounts.merge(reservation.getRoomType(), 1, Integer::sum);
        }
        return roomTypeCounts;
    }

    public int getTotalGuests() {
        return bookingHistory.getAllBookings().stream()
                .mapToInt(Reservation::getPartySize)
                .sum();
    }

    public void printBookingSummary() {
        System.out.println("=== Booking History Report ===");
        System.out.println("Total confirmed bookings: " + getTotalConfirmedBookings());
        System.out.println("Total guests: " + getTotalGuests());
        System.out.println("\nBookings by room type:");
        getBookingsByRoomType().forEach((roomType, count) ->
                System.out.println("  " + roomType + ": " + count + " booking(s)"));
        System.out.println("\nDetailed booking list:");
        bookingHistory.getAllBookings().forEach(reservation ->
                System.out.println("  " + reservation));
    }
}
