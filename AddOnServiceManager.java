/**
 * Manages optional add-on services selected for reservations.
 *
 * This class stores a mapping from reservation ID to the list of selected services.
 * It does not alter booking or inventory state.
 *
 * @author Book My Stay App
 * @version 7.0
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOnServiceManager {

    private final Map<String, List<AddOnService>> servicesByReservationId;

    public AddOnServiceManager() {
        this.servicesByReservationId = new HashMap<>();
    }

    public void addService(Reservation reservation, AddOnService service) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (service == null) {
            throw new IllegalArgumentException("Add-on service cannot be null");
        }
        servicesByReservationId
                .computeIfAbsent(reservation.getReservationId(), key -> new ArrayList<>())
                .add(service);
    }

    public List<AddOnService> getServices(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        return Collections.unmodifiableList(
                servicesByReservationId.getOrDefault(reservation.getReservationId(), List.of())
        );
    }

    public double getAdditionalCost(Reservation reservation) {
        return getServices(reservation).stream()
                .mapToDouble(AddOnService::getPrice)
                .sum();
    }

    public Map<String, List<AddOnService>> getServiceMapping() {
        Map<String, List<AddOnService>> copy = new HashMap<>();
        for (Map.Entry<String, List<AddOnService>> entry : servicesByReservationId.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        return Collections.unmodifiableMap(copy);
    }
}
