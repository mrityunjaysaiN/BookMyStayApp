/**
 * Use Case 4: Room Search & Availability Check
 *
 * This use case enables guests to view available rooms and their details
 * without modifying system state. It demonstrates safe, read-only access to
 * inventory while keeping search logic separate from booking operations.
 *
 * @author Book My Stay App
 * @version 4.0
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseCase4RoomSearch {

    public static void main(String[] args) {
        System.out.println("=== Use Case 4: Room Search & Availability Check ===");
        System.out.println();

        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", 5);
        initialInventory.put("Double", 3);
        initialInventory.put("Suite", 0); // Suite currently unavailable

        RoomInventory inventory = new RoomInventory(initialInventory);

        List<Room> rooms = Arrays.asList(
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        );

        SearchService searchService = new SearchService(inventory, rooms);

        System.out.println("Guest initiates room search...");
        searchService.displayAvailableRooms();

        System.out.println();
        System.out.println("Verifying inventory was not modified during search...");
        System.out.println("Inventory snapshot: " + inventory.getInventorySnapshot());

        System.out.println();
        System.out.println("=== End of Use Case 4 ===");
    }
}