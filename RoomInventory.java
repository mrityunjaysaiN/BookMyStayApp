/**
 * Centralized inventory manager for room availability.
 *
 * This class encapsulates inventory state using a HashMap and provides
 * controlled methods to access and update availability.
 *
 * @author Book My Stay App
 * @version 3.0
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RoomInventory {

    private final Map<String, Integer> availability;

    public RoomInventory(Map<String, Integer> initialAvailability) {
        this.availability = new HashMap<>();
        if (initialAvailability != null) {
            for (Map.Entry<String, Integer> entry : initialAvailability.entrySet()) {
                try {
                    validateRoomCount(entry.getKey(), entry.getValue());
                    this.availability.put(entry.getKey(), entry.getValue());
                } catch (InvalidInventoryException e) {
                    throw new IllegalArgumentException("Invalid initial availability: " + e.getMessage(), e);
                }
            }
        }
    }

    public synchronized int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public synchronized void updateAvailability(String roomType, int count) throws InvalidInventoryException {
        validateRoomCount(roomType, count);
        if (!availability.containsKey(roomType)) {
            throw new InvalidInventoryException("Room type not registered: " + roomType);
        }
        availability.put(roomType, count);
    }

    public void registerRoomType(String roomType, int initialCount) throws InvalidInventoryException {
        validateRoomCount(roomType, initialCount);
        if (availability.containsKey(roomType)) {
            throw new IllegalStateException("Room type already registered: " + roomType);
        }
        availability.put(roomType, initialCount);
    }

    public Map<String, Integer> getInventorySnapshot() {
        return Collections.unmodifiableMap(availability);
    }

    public synchronized void decrementAvailability(String roomType) throws InvalidInventoryException {
        int currentCount = getAvailability(roomType);
        if (currentCount <= 0) {
            throw new InvalidInventoryException("No availability to decrement for room type: " + roomType);
        }
        updateAvailability(roomType, currentCount - 1);
    }

    public synchronized void incrementAvailability(String roomType) throws InvalidInventoryException {
        int currentCount = getAvailability(roomType);
        updateAvailability(roomType, currentCount + 1);
    }

    private void validateRoomCount(String roomType, int count) throws InvalidInventoryException {
        if (roomType == null || roomType.isEmpty()) {
            throw new InvalidInventoryException("Room type must be non-empty");
        }
        if (count < 0) {
            throw new InvalidInventoryException("Availability cannot be negative for " + roomType);
        }
    }
}
