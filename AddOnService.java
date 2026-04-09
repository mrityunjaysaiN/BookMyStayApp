/**
 * Represents an optional add-on service that can be attached to a reservation.
 *
 * Add-on services are separate from booking and inventory logic.
 * They only represent additional guest-selected features and cost.
 *
 * @author Book My Stay App
 * @version 7.0
 */
public class AddOnService {

    private final String name;
    private final double price;

    public AddOnService(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Service name must not be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Service price cannot be negative");
        }
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s ($%.2f)", name, price);
    }
}
