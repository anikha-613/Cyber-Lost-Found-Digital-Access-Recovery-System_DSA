/**
 * Represents a password recovery request submitted by a student.
 * Contains the student's roll number and the account type for which recovery is requested.
 */
public class RecoveryRequest {
    private long rollNumber;
    private String accountType;   // e.g., "ERP", "LMS", "Library", "WiFi"

    public RecoveryRequest(long rollNumber, String accountType) {
        this.rollNumber = rollNumber;
        this.accountType = accountType;
    }

    public long getRollNumber() { return rollNumber; }
    public String getAccountType() { return accountType; }

    @Override
    public String toString() {
        return "Roll: " + rollNumber + " | Account: " + accountType;
    }
}