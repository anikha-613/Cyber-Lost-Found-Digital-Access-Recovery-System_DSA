import java.util.*;

/**
 * Manages all student records using a LinkedList.
 * Provides binary search on roll number by first sorting a copy of the list.
 */
public class StudentManager {
    private LinkedList<Student> students;   // Linked list to store student records

    public StudentManager() {
        students = new LinkedList<>();
    }

    /**
     * Adds a new student to the linked list. Checks for duplicate roll number.
     * @return true if added, false if roll number already exists
     */
    public boolean addStudent(Student s) {
        if (findStudentByRollBinary(s.getRollNumber()) != null) {
            return false;   // duplicate
        }
        students.add(s);
        return true;
    }

    /**
     * Binary search for a student by roll number.
     * Steps:
     * 1. Convert linked list to array.
     * 2. Sort array by roll number (using Arrays.sort).
     * 3. Apply custom binary search.
     *
     * @param roll the 10-digit roll number to search for
     * @return the Student object if found, else null
     */
    public Student findStudentByRollBinary(long roll) {
        // Convert LinkedList to array for sorting and binary search
        Student[] studentArray = students.toArray(new Student[0]);
        
        // Sort array by roll number (ascending)
        Arrays.sort(studentArray, Comparator.comparingLong(Student::getRollNumber));
        
        // Perform binary search manually
        int left = 0, right = studentArray.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long midRoll = studentArray[mid].getRollNumber();
            if (midRoll == roll) {
                return studentArray[mid];
            } else if (midRoll < roll) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null; // not found
    }

    /**
     * Returns the number of registered students.
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Returns the underlying LinkedList (for iteration, etc.)
     */
    public LinkedList<Student> getAllStudents() {
        return students;
    }
}