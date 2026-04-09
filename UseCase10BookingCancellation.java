/**
 * Hotel Booking System v10.0
 *
 * This class demonstrates Use Case 10 by enabling safe booking cancellations
 * with controlled rollback operations using a stack-based approach.
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * @author Book My Stay App
 * @version 10.0
 */
import java.util.HashMap;
import java.util.Map;

public class UseCase10BookingCancellation {

    public static void main(String[] args) {
        Map<String, Integer> initialAvailability = new HashMap<>();
        initialAvailability.put("Single", 3);
        initialAvailability.put("Double", 2);
        initialAvailability.put("Suite", 1);

        RoomInventory inventory = new RoomInventory(initialAvailability);
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);
        BookingHistory bookingHistory = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, bookingHistory, allocationService);

        // Create and confirm some reservations
        try {
            Reservation res1 = new Reservation("Alice", "Single", 1);
            Reservation res2 = new Reservation("Bob", "Double", 2);
            Reservation res3 = new Reservation("Charlie", "Suite", 3);

            requestQueue.submitRequest(res1);
            requestQueue.submitRequest(res2);
            requestQueue.submitRequest(res3);

            System.out.println("Welcome to Book My Stay App - Use Case 10");
            System.out.println("Hotel Booking System v10.0\n");
            System.out.println("Processing initial bookings...\n");

            while (requestQueue.hasPendingRequests()) {
                Reservation reservation = requestQueue.pollNextRequest();
                try {
                    String roomId = allocationService.allocateRoom(reservation);
                    bookingHistory.addConfirmedBooking(reservation);
                    System.out.println("✓ Confirmed: " + reservation.getGuestName() + " - " + roomId);
                } catch (InvalidBookingException | InvalidInventoryException e) {
                    System.out.println("✗ Failed to allocate: " + reservation.getGuestName() + " - " + e.getMessage());
                }
            }

            System.out.println("\nInitial inventory state:");
            printInventory(inventory);

            // Demonstrate cancellations
            System.out.println("\n=== Testing Booking Cancellations ===");

            // Cancel Alice's reservation
            try {
                boolean cancelled = cancellationService.cancelReservation(res1);
                if (cancelled) {
                    System.out.println("✓ Cancelled: " + res1.getGuestName() + "'s reservation");
                }
            } catch (InvalidBookingException | InvalidInventoryException e) {
                System.out.println("✗ Failed to cancel: " + res1.getGuestName() + " - " + e.getMessage());
            }

            System.out.println("\nInventory after cancellation:");
            printInventory(inventory);

            // Try to cancel the same reservation again (should fail)
            try {
                cancellationService.cancelReservation(res1);
            } catch (InvalidBookingException | InvalidInventoryException e) {
                System.out.println("✓ Prevented duplicate cancellation: " + e.getMessage());
            }

            // Try to cancel a non-existent reservation
            try {
                Reservation fakeRes = new Reservation("NonExistent", "Single", 1);
                cancellationService.cancelReservation(fakeRes);
            } catch (InvalidBookingException | InvalidInventoryException e) {
                System.out.println("✓ Prevented invalid cancellation: " + e.getMessage());
            }

            System.out.println("\nFinal cancellation summary:");
            System.out.println("Total cancellations: " + cancellationService.getCancellationCount());
            System.out.println("Released room IDs: " + cancellationService.getReleasedRoomIds());

        } catch (InvalidBookingException e) {
            System.out.println("Error creating reservation: " + e.getMessage());
        }
    }

    private static void printInventory(RoomInventory inventory) {
        for (Map.Entry<String, Integer> entry : inventory.getInventorySnapshot().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " room(s) available");
        }
    }
}
