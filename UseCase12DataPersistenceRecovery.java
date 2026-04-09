/**
 * Use Case 12: Data Persistence & System Recovery
 *
 * This use case demonstrates persistence and recovery concepts by ensuring
 * that critical system state survives application restarts. The system
 * serializes booking history and inventory state to a file, then restores
 * it during startup, transitioning from in-memory to durable system design.
 *
 * @author Book My Stay App
 * @version 12.0
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {
        System.out.println("=== Use Case 12: Data Persistence & System Recovery ===");
        System.out.println();

        PersistenceService persistenceService = new PersistenceService();

        // Phase 1: Initialize or restore system state
        System.out.println("Phase 1: System Initialization");
        SystemState initialState = initializeOrRestoreState(persistenceService);
        RoomInventory inventory = initialState.getInventory();
        BookingHistory bookingHistory = initialState.getBookingHistory();

        System.out.println("Initial state:");
        System.out.println("Inventory: " + inventory);
        System.out.println("Booking History: " + bookingHistory.getTotalBookings() + " bookings");
        System.out.println();

        // Phase 2: Perform some booking operations
        System.out.println("Phase 2: Performing Booking Operations");
        performBookingOperations(inventory, bookingHistory);
        System.out.println();

        // Phase 3: Save system state before shutdown
        System.out.println("Phase 3: System Shutdown - Saving State");
        saveSystemState(persistenceService, inventory, bookingHistory);
        System.out.println();

        // Phase 4: Simulate system restart and recovery
        System.out.println("Phase 4: System Restart - Restoring State");
        SystemState restoredState = simulateSystemRestart(persistenceService);

        if (restoredState != null) {
            RoomInventory restoredInventory = restoredState.getInventory();
            BookingHistory restoredHistory = restoredState.getBookingHistory();

            System.out.println("Restored state:");
            System.out.println("Inventory: " + restoredInventory);
            System.out.println("Booking History: " + restoredHistory.getTotalBookings() + " bookings");
            System.out.println();

            // Phase 5: Verify state integrity
            System.out.println("Phase 5: State Integrity Verification");
            verifyStateIntegrity(inventory, bookingHistory, restoredInventory, restoredHistory);
        } else {
            System.out.println("Failed to restore system state!");
        }

        System.out.println();
        System.out.println("=== End of Use Case 12 ===");
    }

    private static SystemState initializeOrRestoreState(PersistenceService persistenceService) {
        try {
            SystemState savedState = persistenceService.loadSystemState();
            if (savedState != null) {
                System.out.println("Restored previous system state.");
                return savedState;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading saved state: " + e.getMessage());
            System.out.println("Starting with fresh state.");
        }

        // Create fresh state
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", 10);
        initialInventory.put("Double", 8);
        initialInventory.put("Suite", 5);

        RoomInventory inventory = new RoomInventory(initialInventory);
        BookingHistory bookingHistory = new BookingHistory();

        System.out.println("Created fresh system state.");
        return new SystemState(inventory, bookingHistory);
    }

    private static void performBookingOperations(RoomInventory inventory, BookingHistory bookingHistory) {
        // Simulate some booking operations
        try {
            // Create reservations
            Reservation res1 = new Reservation("Alice", "Single", 1);
            Reservation res2 = new Reservation("Bob", "Double", 2);
            Reservation res3 = new Reservation("Charlie", "Suite", 3);

            // Simulate allocation (in a real system, this would use RoomAllocationService)
            inventory.decrementAvailability("Single");
            inventory.decrementAvailability("Double");
            inventory.decrementAvailability("Suite");

            // Add to booking history
            bookingHistory.addConfirmedBooking(res1);
            bookingHistory.addConfirmedBooking(res2);
            bookingHistory.addConfirmedBooking(res3);

            System.out.println("Performed 3 booking operations:");
            System.out.println("- Alice booked Single room");
            System.out.println("- Bob booked Double room");
            System.out.println("- Charlie booked Suite room");

        } catch (InvalidBookingException | InvalidInventoryException e) {
            System.out.println("Error during booking operations: " + e.getMessage());
        }
    }

    private static void saveSystemState(PersistenceService persistenceService,
                                      RoomInventory inventory, BookingHistory bookingHistory) {
        SystemState stateToSave = new SystemState(inventory, bookingHistory);
        try {
            persistenceService.saveSystemState(stateToSave);
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    private static SystemState simulateSystemRestart(PersistenceService persistenceService) {
        try {
            // Simulate restart by loading the state again
            return persistenceService.loadSystemState();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during system restart simulation: " + e.getMessage());
            return null;
        }
    }

    private static void verifyStateIntegrity(RoomInventory originalInventory, BookingHistory originalHistory,
                                           RoomInventory restoredInventory, BookingHistory restoredHistory) {
        boolean inventoryMatch = true;
        boolean historyMatch = true;

        // Check inventory
        try {
            if (originalInventory.getAvailability("Single") != restoredInventory.getAvailability("Single") ||
                originalInventory.getAvailability("Double") != restoredInventory.getAvailability("Double") ||
                originalInventory.getAvailability("Suite") != restoredInventory.getAvailability("Suite")) {
                inventoryMatch = false;
            }
        } catch (Exception e) {
            inventoryMatch = false;
        }

        // Check booking history count
        if (originalHistory.getTotalBookings() != restoredHistory.getTotalBookings()) {
            historyMatch = false;
        }

        System.out.println("Inventory integrity: " + (inventoryMatch ? "✓ PASSED" : "✗ FAILED"));
        System.out.println("Booking history integrity: " + (historyMatch ? "✓ PASSED" : "✗ FAILED"));

        if (inventoryMatch && historyMatch) {
            System.out.println("✓ Overall state integrity verification PASSED!");
            System.out.println("System successfully recovered from persistence.");
        } else {
            System.out.println("✗ State integrity verification FAILED!");
        }
    }
}