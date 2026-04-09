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

    public Reservation(String guestName, String roomType, int partySize) throws InvalidBookingException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be null or empty");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be null or empty");
        }
        if (partySize <= 0) {
            throw new InvalidBookingException("Party size must be at least 1");
        }
        if (partySize > 10) {
            throw new InvalidBookingException("Party size cannot exceed 10 guests");
        }
        // Validate room type against known types
        if (!isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType + ". Valid types: Single, Double, Suite");
        }
        this.reservationId = generateReservationId(guestName, roomType);
        this.guestName = guestName.trim();
        this.roomType = roomType.trim();
        this.partySize = partySize;
    }

    private boolean isValidRoomType(String roomType) {
        return "Single".equalsIgnoreCase(roomType) ||
               "Double".equalsIgnoreCase(roomType) ||
               "Suite".equalsIgnoreCase(roomType);
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
