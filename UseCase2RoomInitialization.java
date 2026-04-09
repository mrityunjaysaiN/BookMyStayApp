/**
 * Hotel Booking System v2.0
 *
 * This class demonstrates Use Case 2 by creating room models and displaying
 * static availability for each room type.
 *
 * Use Case 2: Basic Room Types & Static Availability
 *
 * @author Book My Stay App
 * @version 2.0
 */
public class UseCase2RoomInitialization {

    public static void main(String[] args) {
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        int singleRoomAvailability = 10;
        int doubleRoomAvailability = 5;
        int suiteRoomAvailability = 2;

        System.out.println("Welcome to Book My Stay App - Use Case 2");
        System.out.println("Hotel Booking System v2.0\n");

        printRoomAvailability(singleRoom, singleRoomAvailability);
        printRoomAvailability(doubleRoom, doubleRoomAvailability);
        printRoomAvailability(suiteRoom, suiteRoomAvailability);
    }

    private static void printRoomAvailability(Room room, int availability) {
        System.out.println(room.toString());
        System.out.println("Availability: " + availability + " room(s) available\n");
    }
}
