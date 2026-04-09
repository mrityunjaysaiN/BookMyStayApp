/**
 * Hotel Booking System v5.0
 *
 * This class demonstrates Use Case 5 by collecting booking requests in a
 * first-come-first-served queue without modifying inventory state.
 *
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * @author Book My Stay App
 * @version 5.0
 */
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.submitRequest(new Reservation("Alice", "Single", 1));
        requestQueue.submitRequest(new Reservation("Bob", "Double", 2));
        requestQueue.submitRequest(new Reservation("Charlie", "Suite", 3));
        requestQueue.submitRequest(new Reservation("Dana", "Double", 2));

        System.out.println("Welcome to Book My Stay App - Use Case 5");
        System.out.println("Hotel Booking System v5.0\n");
        System.out.println("Booking requests have been received and queued.");
        System.out.println("No room allocation or inventory changes are performed at intake.\n");

        printQueueState(requestQueue);

        System.out.println("Processing requests in arrival order...\n");
        while (requestQueue.hasPendingRequests()) {
            Reservation next = requestQueue.pollNextRequest();
            System.out.println("Next request: " + next);
        }

        System.out.println("All booking requests have been prepared for allocation.");
    }

    private static void printQueueState(BookingRequestQueue requestQueue) {
        System.out.println(requestQueue);
        int position = 1;
        for (Reservation request : requestQueue.getPendingRequests()) {
            System.out.println(position + ". " + request);
            position++;
        }
        System.out.println();
    }
}
