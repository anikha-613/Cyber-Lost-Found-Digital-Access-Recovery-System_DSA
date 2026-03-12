import java.util.*;

/**
 * Manages admin accounts. For simplicity, we allow only one admin at a time.
 */
public class AdminManager {
    private Admin admin;   // single admin (can be extended to list if needed)

    /**
     * Checks whether an admin has been registered.
     */
    public boolean hasAdmin() {
        return admin != null;
    }

    /**
     * Registers a new admin.
     * @return true if successful (no previous admin)
     */
    public boolean registerAdmin(int id, String name, String password) {
        if (admin != null) {
            return false;   // admin already exists
        }
        // Validate 5-digit ID
        if (id < 10000 || id > 99999) {
            System.out.println("Admin ID must be exactly 5 digits.");
            return false;
        }
        // Password strength can be checked here (optional)
        admin = new Admin(id, name, password);
        return true;
    }

    /**
     * Attempts admin login.
     * @return true if credentials match the registered admin
     */
    public boolean login(int id, String password) {
        return admin != null && admin.getId() == id && admin.getPassword().equals(password);
    }

    /**
     * Returns the admin's name (if logged in).
     */
    public String getAdminName() {
        return admin != null ? admin.getName() : null;
    }
}