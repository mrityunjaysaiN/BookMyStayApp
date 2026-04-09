/**
 * Manages booking requests in arrival order using a queue.
 *
 * This class preserves the first-come-first-served ordering of reservations
 * and does not mutate room inventory during intake.
 *
 * @author Book My Stay App
 * @version 5.0
 */
import java.util.LinkedList;
import java.util.Queue;

public class BookingRequestQueue {

    private final Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        this.requestQueue = new LinkedList<>();
    }

    public synchronized void submitRequest(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        requestQueue.add(reservation);
        notifyAll(); // Notify waiting threads
    }

    public synchronized Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    public synchronized boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }

    public synchronized Reservation pollNextRequest() {
        while (requestQueue.isEmpty()) {
            try {
                wait(); // Wait for requests to be submitted
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return requestQueue.poll();
    }

    public int size() {
        return requestQueue.size();
    }

    public java.util.List<Reservation> getPendingRequests() {
        return new java.util.ArrayList<>(requestQueue);
    }

    @Override
    public String toString() {
        return String.format("BookingRequestQueue[pending=%d]", requestQueue.size());
    }
}
