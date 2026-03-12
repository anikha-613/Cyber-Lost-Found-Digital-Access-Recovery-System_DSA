import java.util.Scanner;

/**
 * Main driver class for the Cyber Lost & Found system.
 * Provides a terminal menu and coordinates all operations.
 */
public class Main {
    private static StudentManager studentManager = new StudentManager();
    private static RequestQueue requestQueue = new RequestQueue();
    private static AdminManager adminManager = new AdminManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Check if admin exists; if not, prompt to create one
        if (!adminManager.hasAdmin()) {
            setupAdmin();
        }

        while (true) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> registerStudent();
                case 2 -> addAccount();
                case 3 -> showAccounts();
                case 4 -> requestPasswordRecovery();
                case 5 -> adminPortal();
                case 6 -> failedLoginAttempt();
                case 7 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n--- Cyber Lost & Found System ---");
        System.out.println("1. Register Student");
        System.out.println("2. Add Account");
        System.out.println("3. Show Accounts");
        System.out.println("4. Request Password Recovery");
        System.out.println("5. Admin Portal");
        System.out.println("6. Failed Login Attempt");
        System.out.println("7. Exit");
    }

    // ------------------- Admin Setup (first run) -------------------
    private static void setupAdmin() {
        System.out.println("\n--- First Time Setup: Create Admin Account ---");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        int id;
        while (true) {
            System.out.print("Enter a 5-digit admin ID: ");
            String idStr = scanner.nextLine();
            if (idStr.length() == 5 && idStr.matches("\\d{5}")) {
                id = Integer.parseInt(idStr);
                break;
            }
            System.out.println("Invalid ID. Must be exactly 5 digits.");
        }
        System.out.print("Enter a password (min 6 chars, letters and numbers): ");
        String password = scanner.nextLine();
        // Optional: validate password strength
        if (password.length() < 6 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            System.out.println("Weak password. Using default 'admin123'.");
            password = "admin123";
        }
        if (adminManager.registerAdmin(id, name, password)) {
            System.out.println("Admin registered successfully!");
        } else {
            System.out.println("Admin registration failed (should not happen).");
        }
    }

    // ------------------- Admin Portal -------------------
    private static void adminPortal() {
        System.out.println("\n--- Admin Portal ---");
        // Login
        System.out.print("Enter admin ID: ");
        int id = getIntInput("");
        System.out.print("Enter password: ");
        String pwd = scanner.nextLine();

        if (!adminManager.login(id, pwd)) {
            System.out.println("Invalid credentials.");
            return;
        }

        // Logged in
        System.out.println("Welcome, " + adminManager.getAdminName() + "!");
        while (true) {
            printAdminMenu();
            int choice = getIntInput("Enter choice: ");
            switch (choice) {
                case 1 -> viewPendingRequests();
                case 2 -> processNextRequest();
                case 3 -> viewStatistics();
                case 4 -> {
                    System.out.println("Logging out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printAdminMenu() {
        System.out.println("\n--- Admin Dashboard ---");
        System.out.println("1. View all pending requests");
        System.out.println("2. Process next request");
        System.out.println("3. View statistics");
        System.out.println("4. Logout");
    }

    private static void viewPendingRequests() {
        requestQueue.viewRequests();
    }

    private static void processNextRequest() {
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }
        RecoveryRequest req = requestQueue.processNext(); // peek and remove
        if (req == null) return;

        System.out.println("\nProcessing request: " + req);
        Student student = studentManager.findStudentByRollBinary(req.getRollNumber());
        if (student == null) {
            System.out.println("Error: Student not found. Request removed.");
            return;
        }
        Account account = student.getAccountByType(req.getAccountType());
        if (account == null) {
            System.out.println("Error: No " + req.getAccountType() + " account for this student. Request removed.");
            return;
        }

        System.out.println("Student: " + student.getRollNumber());
        System.out.println("Account details: " + account);
        System.out.print("Accept this request? (y/n): ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            // Generate new password
            String newPwd = "new" + (int)(Math.random() * 10000);
            account.setPassword(newPwd);
            System.out.println("Password reset successful. New password: " + newPwd);
            // Optional: log action
        } else {
            System.out.println("Request rejected and removed from queue.");
        }
    }

    private static void viewStatistics() {
        System.out.println("\n--- System Statistics ---");
        System.out.println("Registered students: " + studentManager.getStudentCount());
        System.out.println("Total pending requests: " + requestQueue.getTotalRequests());
        System.out.println("Requests by account type:");
        String[] types = {"ERP", "LMS", "Library", "WiFi"};
        for (String type : types) {
            System.out.println("  " + type + ": " + requestQueue.countByAccountType(type));
        }
    }

    // ------------------- Student Operations -------------------
    private static void registerStudent() {
        System.out.println("\n--- Student Registration ---");
        long roll = getRollNumberInput();
        if (roll == -1) return;

        String password = getPasswordInput();
        if (password == null) return;

        Student student = new Student(roll, password);
        if (studentManager.addStudent(student)) {
            System.out.println("Student registered successfully!");
        } else {
            System.out.println("Roll number already exists. Registration failed.");
        }
    }

    private static void addAccount() {
        System.out.println("\n--- Add Account ---");
        long roll = getRollNumberInput();
        if (roll == -1) return;

        Student student = studentManager.findStudentByRollBinary(roll);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter account type (ERP/LMS/Library/WiFi): ");
        String type = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter account password: ");
        String accPassword = scanner.nextLine();

        Account account = new Account(type, username, accPassword);
        student.addAccount(account);
        System.out.println("Account added successfully.");
    }

    private static void showAccounts() {
        System.out.println("\n--- Show Accounts ---");
        long roll = getRollNumberInput();
        if (roll == -1) return;

        Student student = studentManager.findStudentByRollBinary(roll);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        var accounts = student.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts saved for this student.");
        } else {
            System.out.println("Saved accounts:");
            for (Account acc : accounts) {
                System.out.println("  " + acc);
            }
        }
    }

    private static void requestPasswordRecovery() {
        System.out.println("\n--- Request Password Recovery ---");
        long roll = getRollNumberInput();
        if (roll == -1) return;

        Student student = studentManager.findStudentByRollBinary(roll);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter account type you need recovery for (ERP/LMS/Library/WiFi): ");
        String accType = scanner.nextLine();
        // Verify that the student actually has such an account (optional)
        Account acc = student.getAccountByType(accType);
        if (acc == null) {
            System.out.println("You do not have an account of type " + accType + ". Request not submitted.");
            return;
        }

        RecoveryRequest req = new RecoveryRequest(roll, accType);
        requestQueue.addRequest(req);
        System.out.println("Recovery request queued. Admin will process it.");
    }

    private static void failedLoginAttempt() {
        System.out.println("\n--- Failed Login Attempt ---");
        long roll = getRollNumberInput();
        if (roll == -1) return;

        Student student = studentManager.findStudentByRollBinary(roll);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        student.incrementFailedAttempts();
        System.out.println("Failed attempt recorded. Total failed attempts for roll " + roll + ": " + student.getFailedAttempts());
        if (student.getFailedAttempts() >= 3) {
            System.out.println("WARNING: Account locked due to multiple failed attempts. Contact admin.");
        }
    }

    // ------------------- Input Helpers -------------------
    private static long getRollNumberInput() {
        System.out.print("Enter 10-digit roll number: ");
        String input = scanner.nextLine();
        if (input.length() != 10 || !input.matches("\\d{10}")) {
            System.out.println("Invalid roll number. Must be exactly 10 digits.");
            return -1;
        }
        return Long.parseLong(input);
    }

    private static String getPasswordInput() {
        System.out.print("Enter password (min 6 chars, must contain letters and numbers): ");
        String pwd = scanner.nextLine();
        if (pwd.length() < 6 || !pwd.matches(".*[a-zA-Z].*") || !pwd.matches(".*[0-9].*")) {
            System.out.println("Password must be at least 6 characters and include both letters and numbers.");
            return null;
        }
        return pwd;
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }
}