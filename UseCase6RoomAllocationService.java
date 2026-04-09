/**
 * Hotel Booking System v6.0
 *
 * This class demonstrates Use Case 6 by confirming reservations and allocating
 * unique room IDs while updating centralized inventory immediately.
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @author Book My Stay App
 * @version 6.0
 */
import java.util.HashMap;
import java.util.Map;

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {
        Map<String, Integer> initialAvailability = new HashMap<>();
        initialAvailability.put("Single", 2);
        initialAvailability.put("Double", 2);
        initialAvailability.put("Suite", 1);

        RoomInventory inventory = new RoomInventory(initialAvailability);
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);

        requestQueue.submitRequest(new Reservation("Alice", "Single", 1));
        requestQueue.submitRequest(new Reservation("Bob", "Double", 2));
        requestQueue.submitRequest(new Reservation("Charlie", "Suite", 3));
        requestQueue.submitRequest(new Reservation("Dana", "Single", 1));

        System.out.println("Welcome to Book My Stay App - Use Case 6");
        System.out.println("Hotel Booking System v6.0\n");
        System.out.println("Queued booking requests are now being confirmed with room allocation.");
        System.out.println("Unique room IDs are generated and inventory is updated immediately.\n");

        processBookingRequests(requestQueue, allocationService);
        printFinalInventory(inventory);
    }

    private static void processBookingRequests(BookingRequestQueue requestQueue, RoomAllocationService allocationService) {
        int processed = 0;
        while (requestQueue.hasPendingRequests()) {
            Reservation reservation = requestQueue.pollNextRequest();
            try {
                String roomId = allocationService.allocateRoom(reservation);
                System.out.println("Confirmed reservation for " + reservation.getGuestName()
                        + ": " + reservation.getRoomType() + " assigned " + roomId);
                processed++;
            } catch (IllegalStateException exception) {
                System.out.println("Unable to confirm reservation for " + reservation.getGuestName()
                        + ": " + exception.getMessage());
            }
        }

        System.out.println();
        System.out.println("Processed " + processed + " booking request(s).");
    }

    private static void printFinalInventory(RoomInventory inventory) {
        System.out.println("\nInventory after allocation:");
        for (Map.Entry<String, Integer> entry : inventory.getInventorySnapshot().entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue() + " room(s) remaining");
        }
    }
}
