/**
 * Provides read-only room search functionality.
 *
 * This service queries inventory availability and room metadata without
 * mutating any system state. It ensures search operations remain separate
 * from booking and allocation logic.
 *
 * @author Book My Stay App
 * @version 4.0
 */
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchService {

    private final RoomInventory inventory;
    private final Map<String, Room> roomsByType;

    public SearchService(RoomInventory inventory, List<Room> roomTypes) {
        if (inventory == null || roomTypes == null) {
            throw new IllegalArgumentException("Inventory and room types cannot be null");
        }
        this.inventory = inventory;
        this.roomsByType = new LinkedHashMap<>();
        for (Room room : roomTypes) {
            if (room != null) {
                roomsByType.put(room.getName(), room);
            }
        }
    }

    /**
     * Retrieves only the room types that currently have availability.
     *
     * @return ordered map of available room types to their room objects
     */
    public Map<String, Room> getAvailableRooms() {
        return roomsByType.entrySet().stream()
                .filter(entry -> inventory.getAvailability(entry.getKey()) > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }

    /**
     * Displays available rooms and their current pricing without modifying inventory.
     */
    public void displayAvailableRooms() {
        Map<String, Room> availableRooms = getAvailableRooms();

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms are currently available.");
            return;
        }

        System.out.println("Available room types:");
        for (Room room : availableRooms.values()) {
            int availability = inventory.getAvailability(room.getName());
            System.out.printf("- %s: %s, %d available, $%.2f per night\n",
                    room.getName(), room.toString(), availability, room.getPricePerNight());
        }
    }
}