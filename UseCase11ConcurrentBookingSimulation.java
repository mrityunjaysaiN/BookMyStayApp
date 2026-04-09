/**
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * This use case demonstrates the importance of thread safety in a multi-threaded
 * environment where multiple users attempt to book rooms simultaneously.
 *
 * It shows how synchronized methods prevent race conditions that could lead to
 * overbooking or inconsistent inventory states.
 *
 * @author Book My Stay App
 * @version 11.0
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class UseCase11ConcurrentBookingSimulation {

    private static final int NUM_THREADS = 10;
    private static final int INITIAL_AVAILABILITY = 5;

    public static void main(String[] args) {
        System.out.println("=== Use Case 11: Concurrent Booking Simulation (Thread Safety) ===");
        System.out.println();

        // Initialize services
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", INITIAL_AVAILABILITY);
        initialInventory.put("Double", INITIAL_AVAILABILITY);
        initialInventory.put("Suite", INITIAL_AVAILABILITY);
        RoomInventory inventory = new RoomInventory(initialInventory);
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);
        BookingHistory bookingHistory = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, bookingHistory, allocationService);

        // Set up initial inventory
        try {
            inventory.updateAvailability("Single", INITIAL_AVAILABILITY);
            inventory.updateAvailability("Double", INITIAL_AVAILABILITY);
            inventory.updateAvailability("Suite", INITIAL_AVAILABILITY);
        } catch (InvalidInventoryException e) {
            System.out.println("Error setting up inventory: " + e.getMessage());
            return;
        }

        System.out.println("Initial Inventory:");
        System.out.println("Single Rooms: " + inventory.getAvailability("Single"));
        System.out.println("Double Rooms: " + inventory.getAvailability("Double"));
        System.out.println("Suite Rooms: " + inventory.getAvailability("Suite"));
        System.out.println();

        // Create multiple booking threads
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(NUM_THREADS);
        List<BookingThread> threads = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            String roomType = getRoomTypeForThread(i);
            BookingThread thread = new BookingThread(
                "Thread-" + i,
                roomType,
                requestQueue,
                allocationService,
                bookingHistory,
                startLatch,
                endLatch
            );
            threads.add(thread);
            thread.start();
        }

        // Start all threads simultaneously
        System.out.println("Starting " + NUM_THREADS + " concurrent booking threads...");
        startLatch.countDown();

        // Wait for all threads to complete
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Simulation interrupted");
            return;
        }

        System.out.println();
        System.out.println("Simulation completed!");
        System.out.println();

        // Display final results
        System.out.println("Final Inventory:");
        System.out.println("Single Rooms: " + inventory.getAvailability("Single"));
        System.out.println("Double Rooms: " + inventory.getAvailability("Double"));
        System.out.println("Suite Rooms: " + inventory.getAvailability("Suite"));
        System.out.println();

        System.out.println("Booking History:");
        System.out.println("Total confirmed bookings: " + bookingHistory.getTotalBookings());
        System.out.println("Single room bookings: " + bookingHistory.getBookingsByRoomType("Single").size());
        System.out.println("Double room bookings: " + bookingHistory.getBookingsByRoomType("Double").size());
        System.out.println("Suite room bookings: " + bookingHistory.getBookingsByRoomType("Suite").size());
        System.out.println();

        // Verify thread safety
        int totalBookings = bookingHistory.getTotalBookings();
        int totalAllocated = (INITIAL_AVAILABILITY - inventory.getAvailability("Single")) +
                           (INITIAL_AVAILABILITY - inventory.getAvailability("Double")) +
                           (INITIAL_AVAILABILITY - inventory.getAvailability("Suite"));

        System.out.println("Thread Safety Verification:");
        System.out.println("Total bookings in history: " + totalBookings);
        System.out.println("Total rooms allocated: " + totalAllocated);

        if (totalBookings == totalAllocated) {
            System.out.println("✓ Thread safety maintained - no race conditions detected!");
        } else {
            System.out.println("✗ Race condition detected - inventory inconsistency!");
        }

        System.out.println();
        System.out.println("=== End of Use Case 11 ===");
    }

    private static String getRoomTypeForThread(int threadIndex) {
        switch (threadIndex % 3) {
            case 0: return "Single";
            case 1: return "Double";
            case 2: return "Suite";
            default: return "Single";
        }
    }

    static class BookingThread extends Thread {
        private final String roomType;
        private final BookingRequestQueue requestQueue;
        private final RoomAllocationService allocationService;
        private final BookingHistory bookingHistory;
        private final CountDownLatch startLatch;
        private final CountDownLatch endLatch;

        public BookingThread(String name, String roomType, BookingRequestQueue requestQueue,
                           RoomAllocationService allocationService, BookingHistory bookingHistory,
                           CountDownLatch startLatch, CountDownLatch endLatch) {
            super(name);
            this.roomType = roomType;
            this.requestQueue = requestQueue;
            this.allocationService = allocationService;
            this.bookingHistory = bookingHistory;
            this.startLatch = startLatch;
            this.endLatch = endLatch;
        }

        @Override
        public void run() {
            try {
                // Wait for all threads to be ready
                startLatch.await();

                // Create and submit booking request
                Reservation reservation;
                try {
                    reservation = new Reservation("Guest-" + getName(), roomType, 1);
                } catch (InvalidBookingException e) {
                    System.out.println(getName() + " failed to create reservation: " + e.getMessage());
                    return;
                }
                requestQueue.submitRequest(reservation);

                // Process the request (simulate allocation)
                Reservation processedRequest = requestQueue.pollNextRequest();
                if (processedRequest != null) {
                    try {
                        String roomId = allocationService.allocateRoom(processedRequest);
                        bookingHistory.addConfirmedBooking(processedRequest);
                        System.out.println(getName() + " successfully booked " + roomType + " room (ID: " + roomId + ")");
                    } catch (InvalidBookingException | InvalidInventoryException e) {
                        System.out.println(getName() + " failed to book " + roomType + " room: " + e.getMessage());
                    }
                } else {
                    System.out.println(getName() + " could not process booking request");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " was interrupted");
            } finally {
                endLatch.countDown();
            }
        }
    }
}