/**
 * Represents a general hotel room.
 *
 * This class is abstract because room details vary by concrete room type.
 * It provides shared properties and behavior for all room types.
 *
 * @author Book My Stay App
 * @version 2.0
 */
public abstract class Room {

    private final String name;
    private final int beds;
    private final int sizeSqFt;
    private final double pricePerNight;

    protected Room(String name, int beds, int sizeSqFt, double pricePerNight) {
        this.name = name;
        this.beds = beds;
        this.sizeSqFt = sizeSqFt;
        this.pricePerNight = pricePerNight;
    }

    public String getName() {
        return name;
    }

    public int getBeds() {
        return beds;
    }

    public int getSizeSqFt() {
        return sizeSqFt;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @Override
    public String toString() {
        return String.format("%s room with %d bed(s), %d sq ft, $%.2f per night",
                name, beds, sizeSqFt, pricePerNight);
    }
}
