/**
 * Hotel Booking System v3.0
 *
 * This class demonstrates Use Case 3 by initializing centralized room inventory
 * using a HashMap and displaying inventory state through controlled access.
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * @author Book My Stay App
 * @version 3.0
 */
import java.util.HashMap;
import java.util.Map;

public class UseCase3InventorySetup {

    public static void main(String[] args) {
        Map<String, Integer> initialAvailability = new HashMap<>();
        initialAvailability.put("Single", 10);
        initialAvailability.put("Double", 5);
        initialAvailability.put("Suite", 2);

        RoomInventory inventory = new RoomInventory(initialAvailability);

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        System.out.println("Welcome to Book My Stay App - Use Case 3");
        System.out.println("Hotel Booking System v3.0\n");

        printRoomStatus(singleRoom, inventory);
        printRoomStatus(doubleRoom, inventory);
        printRoomStatus(suiteRoom, inventory);

        System.out.println("Updating inventory through controlled methods...\n");
        try {
            inventory.updateAvailability("Double", 4);
            inventory.updateAvailability("Suite", 1);
        } catch (InvalidInventoryException e) {
            System.out.println("Error updating inventory: " + e.getMessage());
            return;
        }

        printInventorySnapshot(inventory);
    }

    private static void printRoomStatus(Room room, RoomInventory inventory) {
        System.out.println(room.toString());
        System.out.println("Availability: " + inventory.getAvailability(room.getName()) + " room(s) available\n");
    }

    private static void printInventorySnapshot(RoomInventory inventory) {
        System.out.println("Current centralized inventory state:");
        for (Map.Entry<String, Integer> entry : inventory.getInventorySnapshot().entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue() + " room(s)");
        }
    }
}
