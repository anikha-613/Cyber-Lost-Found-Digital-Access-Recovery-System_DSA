import java.util.*;

/**
 * Manages password recovery requests using a Queue (FIFO).
 * Stores RecoveryRequest objects.
 */
public class RequestQueue {
    private Queue<RecoveryRequest> queue;   // Queue for pending requests

    public RequestQueue() {
        queue = new LinkedList<>();   // LinkedList implements Queue
    }

    /**
     * Add a recovery request to the queue.
     */
    public void addRequest(RecoveryRequest req) {
        queue.offer(req);
        System.out.println("Recovery request added: " + req);
    }

    /**
     * Process the next request (dequeue).
     * @return the next RecoveryRequest, or null if queue empty
     */
    public RecoveryRequest processNext() {
        return queue.poll();
    }

    /**
     * View all pending requests without removing them.
     */
    public void viewRequests() {
        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            System.out.println("Pending requests (FIFO order):");
            for (RecoveryRequest req : queue) {
                System.out.println("  " + req);
            }
        }
    }

    /**
     * Returns the number of pending requests for a specific account type.
     */
    public int countByAccountType(String type) {
        int count = 0;
        for (RecoveryRequest req : queue) {
            if (req.getAccountType().equalsIgnoreCase(type)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the total number of pending requests.
     */
    public int getTotalRequests() {
        return queue.size();
    }

    /**
     * Check if queue is empty.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}