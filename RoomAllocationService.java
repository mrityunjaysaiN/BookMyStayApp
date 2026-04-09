/**
 * Processes booking requests and confirms room allocations.
 *
 * This service assigns unique room IDs, prevents duplicate assignments,
 * and updates inventory immediately to maintain consistency.
 *
 * @author Book My Stay App
 * @version 6.0
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoomAllocationService {

    private final RoomInventory inventory;
    private final Set<String> allocatedRoomIds;
    private final Map<String, Set<String>> allocationsByRoomType;
    private final Map<String, Integer> roomTypeCounters;

    public RoomAllocationService(RoomInventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.allocationsByRoomType = new HashMap<>();
        this.roomTypeCounters = new HashMap<>();
    }

    public String allocateRoom(Reservation reservation) throws InvalidBookingException, InvalidInventoryException {
        if (reservation == null) {
            throw new InvalidBookingException("Reservation cannot be null");
        }

        String roomType = reservation.getRoomType();
        int available = inventory.getAvailability(roomType);
        if (available <= 0) {
            throw new InvalidInventoryException("No availability for room type: " + roomType);
        }

        String roomId = generateUniqueRoomId(roomType);
        if (!allocatedRoomIds.add(roomId)) {
            throw new InvalidBookingException("Room ID already allocated: " + roomId);
        }

        allocationsByRoomType.computeIfAbsent(roomType, key -> new HashSet<>()).add(roomId);
        inventory.decrementAvailability(roomType);
        return roomId;
    }

    private String generateUniqueRoomId(String roomType) {
        String prefix = roomType.replaceAll("\\s+", "").toUpperCase();
        prefix = prefix.length() <= 3 ? prefix : prefix.substring(0, 3);
        int nextId = roomTypeCounters.getOrDefault(roomType, 0) + 1;
        roomTypeCounters.put(roomType, nextId);
        return String.format("%s-%03d", prefix, nextId);
    }

    public Set<String> getAllocatedRoomIds(String roomType) {
        return allocationsByRoomType.containsKey(roomType)
                ? Set.copyOf(allocationsByRoomType.get(roomType))
                : Set.of();
    }

    public Set<String> getAllAllocatedRoomIds() {
        return Set.copyOf(allocatedRoomIds);
    }

    public String getAllocatedRoomId(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        String roomType = reservation.getRoomType();
        Set<String> allocatedForType = allocationsByRoomType.get(roomType);
        if (allocatedForType != null) {
            // In a real system, we'd track reservation-to-room mapping
            // For this demo, we'll return the first allocated room of that type
            return allocatedForType.stream().findFirst().orElse(null);
        }
        return null;
    }
}
