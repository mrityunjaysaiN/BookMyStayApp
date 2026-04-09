/**
 * Represents a guest booking request for a specific room type.
 *
 * A Reservation captures the guest's intent without changing inventory state.
 * It is intended to be queued and processed later by an allocation system.
 *
 * @author Book My Stay App
 * @version 5.0
 */
public class Reservation {

    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final int partySize;

    public Reservation(String guestName, String roomType, int partySize) {
        if (guestName == null || guestName.isBlank()) {
            throw new IllegalArgumentException("Guest name must not be empty");
        }
        if (roomType == null || roomType.isBlank()) {
            throw new IllegalArgumentException("Room type must not be empty");
        }
        if (partySize <= 0) {
            throw new IllegalArgumentException("Party size must be at least 1");
        }
        this.reservationId = generateReservationId(guestName, roomType);
        this.guestName = guestName;
        this.roomType = roomType;
        this.partySize = partySize;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getPartySize() {
        return partySize;
    }

    private String generateReservationId(String guestName, String roomType) {
        String normalizedGuest = guestName.trim().replaceAll("\\s+", "_").toUpperCase();
        String normalizedRoom = roomType.trim().replaceAll("\\s+", "_").toUpperCase();
        return String.format("RES-%s-%s-%s", normalizedGuest, normalizedRoom, java.util.UUID.randomUUID().toString().substring(0, 8));
    }

    @Override
    public String toString() {
        return String.format("Reservation[id=%s, guest=%s, roomType=%s, partySize=%d]",
                reservationId, guestName, roomType, partySize);
    }
}
