/**
 * Handles persistence and recovery of system state.
 *
 * This service provides methods to save the current system state to a file
 * and restore it during application startup, ensuring data durability across
 * application restarts.
 *
 * @author Book My Stay App
 * @version 12.0
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenceService {

    private static final String STATE_FILE = "system_state.dat";

    /**
     * Saves the current system state to persistent storage.
     *
     * @param state The system state to persist
     * @throws IOException if an I/O error occurs during saving
     */
    public void saveSystemState(SystemState state) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(STATE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(state);
            System.out.println("System state saved successfully to " + STATE_FILE);
        }
    }

    /**
     * Loads the system state from persistent storage.
     *
     * @return The restored system state, or null if no saved state exists
     * @throws IOException if an I/O error occurs during loading
     * @throws ClassNotFoundException if the saved state cannot be deserialized
     */
    public SystemState loadSystemState() throws IOException, ClassNotFoundException {
        File stateFile = new File(STATE_FILE);
        if (!stateFile.exists()) {
            System.out.println("No saved state file found. Starting with fresh state.");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(stateFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully from " + STATE_FILE);
            return state;
        }
    }

    /**
     * Checks if a saved state file exists.
     *
     * @return true if a state file exists, false otherwise
     */
    public boolean hasSavedState() {
        return new File(STATE_FILE).exists();
    }

    /**
     * Deletes the saved state file if it exists.
     * Useful for testing or resetting the system.
     */
    public void clearSavedState() {
        File stateFile = new File(STATE_FILE);
        if (stateFile.exists() && stateFile.delete()) {
            System.out.println("Saved state file cleared.");
        }
    }
}