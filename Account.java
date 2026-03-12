/**
 * Represents an account (ERP, LMS, Library, WiFi) belonging to a student.
 */
public class Account {
    private String type;      // e.g., "ERP", "LMS", "Library", "WiFi"
    private String username;
    private String password;

    public Account(String type, String username, String password) {
        this.type = type;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getType() { return type; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return type + " - Username: " + username + ", Password: " + password;
    }
}