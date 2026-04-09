/**
 * Hotel Booking System v8.0
 *
 * This class demonstrates Use Case 8 by maintaining booking history and
 * generating reports from confirmed reservation data.
 *
 * Use Case 8: Booking History & Reporting
 *
 * @author Book My Stay App
 * @version 8.0
 */
import java.util.HashMap;
import java.util.Map;

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {
        Map<String, Integer> initialAvailability = new HashMap<>();
        initialAvailability.put("Single", 5);
        initialAvailability.put("Double", 3);
        initialAvailability.put("Suite", 2);

        RoomInventory inventory = new RoomInventory(initialAvailability);
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Simulate booking requests and confirmations
        requestQueue.submitRequest(new Reservation("Alice", "Single", 1));
        requestQueue.submitRequest(new Reservation("Bob", "Double", 2));
        requestQueue.submitRequest(new Reservation("Charlie", "Suite", 3));
        requestQueue.submitRequest(new Reservation("Dana", "Single", 1));
        requestQueue.submitRequest(new Reservation("Eve", "Double", 2));

        System.out.println("Welcome to Book My Stay App - Use Case 8");
        System.out.println("Hotel Booking System v8.0\n");
        System.out.println("Processing booking requests and building history...\n");

        // Process requests and add to history
        while (requestQueue.hasPendingRequests()) {
            Reservation reservation = requestQueue.pollNextRequest();
            try {
                String roomId = allocationService.allocateRoom(reservation);
                bookingHistory.addConfirmedBooking(reservation);
                System.out.println("Confirmed: " + reservation.getGuestName() + " - " + roomId);
            } catch (IllegalStateException exception) {
                System.out.println("Unable to confirm: " + reservation.getGuestName() + " - " + exception.getMessage());
            }
        }

        System.out.println("\nGenerating booking history report...\n");
        reportService.printBookingSummary();
    }
}
