/**
 * Hotel Booking System v7.0
 *
 * This class demonstrates Use Case 7 by attaching optional add-on services to
 * reservations while keeping core booking and inventory state unchanged.
 *
 * Use Case 7: Add-On Service Selection
 *
 * @author Book My Stay App
 * @version 7.0
 */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {
        Reservation reservation1 = new Reservation("Alice", "Single", 1);
        Reservation reservation2 = new Reservation("Bob", "Double", 2);
        Reservation reservation3 = new Reservation("Charlie", "Suite", 3);

        AddOnService breakfast = new AddOnService("Breakfast", 19.99);
        AddOnService airportTransfer = new AddOnService("Airport Transfer", 49.99);
        AddOnService spaPackage = new AddOnService("Spa Package", 79.99);
        AddOnService lateCheckout = new AddOnService("Late Checkout", 29.99);

        AddOnServiceManager serviceManager = new AddOnServiceManager();
        serviceManager.addService(reservation1, breakfast);
        serviceManager.addService(reservation1, lateCheckout);
        serviceManager.addService(reservation2, airportTransfer);
        serviceManager.addService(reservation3, spaPackage);
        serviceManager.addService(reservation3, breakfast);

        System.out.println("Welcome to Book My Stay App - Use Case 7");
        System.out.println("Hotel Booking System v7.0\n");
        System.out.println("Optional add-on services have been selected for reservations.");
        System.out.println("Core booking and inventory state remain unchanged.\n");

        printReservationServices(reservation1, serviceManager);
        printReservationServices(reservation2, serviceManager);
        printReservationServices(reservation3, serviceManager);

        System.out.println("Full reservation-to-service mapping:");
        serviceManager.getServiceMapping().forEach((reservationId, services) -> {
            System.out.println(reservationId + " => " + services);
        });
    }

    private static void printReservationServices(Reservation reservation, AddOnServiceManager serviceManager) {
        System.out.println(reservation);
        System.out.println("Selected services: " + serviceManager.getServices(reservation));
        System.out.println("Additional cost: $" + String.format("%.2f", serviceManager.getAdditionalCost(reservation)) + "\n");
    }
}
