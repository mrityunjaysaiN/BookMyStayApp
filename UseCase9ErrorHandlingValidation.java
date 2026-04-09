/**
 * Hotel Booking System v9.0
 *
 * This class demonstrates Use Case 9 by validating inputs and handling
 * errors gracefully to ensure system reliability.
 *
 * Use Case 9: Error Handling & Validation
 *
 * @author Book My Stay App
 * @version 9.0
 */
import java.util.HashMap;
import java.util.Map;

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {
        Map<String, Integer> initialAvailability = new HashMap<>();
        initialAvailability.put("Single", 2);
        initialAvailability.put("Double", 1);
        initialAvailability.put("Suite", 1);

        RoomInventory inventory = new RoomInventory(initialAvailability);
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);

        System.out.println("Welcome to Book My Stay App - Use Case 9");
        System.out.println("Hotel Booking System v9.0\n");
        System.out.println("Demonstrating error handling and validation...\n");

        // Test invalid reservation creation
        testInvalidReservations();

        // Test valid reservations and allocation
        System.out.println("=== Testing Valid Reservations ===");
        try {
            Reservation validReservation1 = new Reservation("Alice", "Single", 1);
            Reservation validReservation2 = new Reservation("Bob", "Double", 2);
            Reservation validReservation3 = new Reservation("Charlie", "Suite", 3);

            requestQueue.submitRequest(validReservation1);
            requestQueue.submitRequest(validReservation2);
            requestQueue.submitRequest(validReservation3);

            processValidRequests(requestQueue, allocationService);
        } catch (InvalidBookingException e) {
            System.out.println("Unexpected error in valid reservation: " + e.getMessage());
        }

        // Test inventory validation
        System.out.println("\n=== Testing Inventory Validation ===");
        testInventoryValidation(inventory);
    }

    private static void testInvalidReservations() {
        System.out.println("=== Testing Invalid Reservation Creation ===");

        String[] invalidScenarios = {
            "null guest name",
            "empty guest name",
            "null room type",
            "empty room type",
            "invalid room type",
            "zero party size",
            "negative party size",
            "party size too large"
        };

        try {
            new Reservation(null, "Single", 1);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[0] + ": " + e.getMessage());
        }

        try {
            new Reservation("", "Single", 1);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[1] + ": " + e.getMessage());
        }

        try {
            new Reservation("Alice", null, 1);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[2] + ": " + e.getMessage());
        }

        try {
            new Reservation("Alice", "", 1);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[3] + ": " + e.getMessage());
        }

        try {
            new Reservation("Alice", "Penthouse", 1);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[4] + ": " + e.getMessage());
        }

        try {
            new Reservation("Alice", "Single", 0);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[5] + ": " + e.getMessage());
        }

        try {
            new Reservation("Alice", "Single", -1);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[6] + ": " + e.getMessage());
        }

        try {
            new Reservation("Alice", "Single", 15);
        } catch (InvalidBookingException e) {
            System.out.println("✓ " + invalidScenarios[7] + ": " + e.getMessage());
        }

        System.out.println();
    }

    private static void processValidRequests(BookingRequestQueue requestQueue, RoomAllocationService allocationService) {
        while (requestQueue.hasPendingRequests()) {
            Reservation reservation = requestQueue.pollNextRequest();
            try {
                String roomId = allocationService.allocateRoom(reservation);
                System.out.println("✓ Confirmed: " + reservation.getGuestName() + " - " + roomId);
            } catch (InvalidBookingException | InvalidInventoryException e) {
                System.out.println("✗ Failed to allocate for " + reservation.getGuestName() + ": " + e.getMessage());
            }
        }
        System.out.println();
    }

    private static void testInventoryValidation(RoomInventory inventory) {
        try {
            inventory.updateAvailability("Single", -1);
        } catch (InvalidInventoryException e) {
            System.out.println("✓ Negative availability prevented: " + e.getMessage());
        }

        try {
            inventory.updateAvailability("NonExistent", 5);
        } catch (InvalidInventoryException e) {
            System.out.println("✓ Unregistered room type prevented: " + e.getMessage());
        }

        try {
            inventory.decrementAvailability("Suite");
            inventory.decrementAvailability("Suite"); // Should fail
        } catch (InvalidInventoryException e) {
            System.out.println("✓ Over-decrement prevented: " + e.getMessage());
        }

        System.out.println("\nSystem remains stable after all validation tests.");
    }
}
