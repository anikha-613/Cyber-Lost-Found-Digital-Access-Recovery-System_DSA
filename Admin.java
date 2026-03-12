/**
 * Represents an administrator with a 5-digit ID, name, and password.
 */
public class Admin {
    private int id;           // 5-digit ID
    private String name;
    private String password;

    public Admin(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
}