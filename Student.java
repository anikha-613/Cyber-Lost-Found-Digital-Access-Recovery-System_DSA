import java.util.*;

/**
 * Represents a student with roll number, login password, password history (stack),
 * and a list of saved accounts.
 */
public class Student {
    private long rollNumber;                 // 10-digit roll number
    private String password;                  // current login password
    private Stack<String> passwordHistory;    // Stack for previous login passwords
    private List<Account> accounts;           // list of accounts (ERP, LMS, etc.)
    private int failedAttempts;                // counter for failed logins

    public Student(long rollNumber, String password) {
        this.rollNumber = rollNumber;
        this.password = password;
        this.passwordHistory = new Stack<>();   // Stack used for password history
        this.accounts = new ArrayList<>();
        this.failedAttempts = 0;
    }

    // Getters and setters
    public long getRollNumber() { return rollNumber; }
    public String getPassword() { return password; }
    public int getFailedAttempts() { return failedAttempts; }
    public void incrementFailedAttempts() { this.failedAttempts++; }
    public void resetFailedAttempts() { this.failedAttempts = 0; }

    /**
     * Changes the student's login password. Pushes the old password onto the history stack.
     */
    public void changePassword(String newPassword) {
        passwordHistory.push(this.password);   // store old password in stack
        this.password = newPassword;
    }

    /**
     * Adds an account to the student's list. If an account of the same type already exists,
     * it is replaced (you can modify this behaviour as needed).
     */
    public void addAccount(Account acc) {
        // Optional: remove existing account of same type
        accounts.removeIf(a -> a.getType().equalsIgnoreCase(acc.getType()));
        accounts.add(acc);
    }

    /**
     * Returns the first account matching the given type (case‑insensitive).
     * @return Account or null if not found
     */
    public Account getAccountByType(String type) {
        for (Account acc : accounts) {
            if (acc.getType().equalsIgnoreCase(type)) {
                return acc;
            }
        }
        return null;
    }

    /**
     * Returns a copy of the accounts list.
     */
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    /**
     * Displays password history (optional, for debugging).
     */
    public void showPasswordHistory() {
        System.out.println("Password history (most recent old first): " + passwordHistory);
    }

    @Override
    public String toString() {
        return "Roll: " + rollNumber + ", Accounts: " + accounts.size();
    }
}