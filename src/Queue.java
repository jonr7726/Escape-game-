import java.util.ArrayList;

/**
 * Generic queue class, can queue and dequeue objects, dequeue will return the fist object queued (rather than the most recent)
 */
public class Queue<T> {

    private ArrayList<T> queue;
    
    /**
     * constructor initilizes instance variables
     */
    public Queue() {
        queue = new ArrayList<T>();
    }
    
    /**
     * constructor initilizes instance variables, also initilizes first queue object
     */
    public Queue(T objectStart) {
        queue = new ArrayList<T>();
        enqueue(objectStart);
    }

    /**
     * adds obeject to queue
     */
    public void enqueue(T object) {
        if(queue.indexOf(object) == -1) {
            queue.add(object);
        }
    }
    
    /**
     * returns the first object in queue and deletes it
     */
    public T dequeue() {
        T object = queue.get(0);
        queue.remove(object);
        return object;
    }
    
    /**
     * returns if queue is empty
     */
    public boolean isEmpty() {
        return queue.size() == 0;
    }
    
    /**
     * clears queue
     */
    public void clear() {
        queue.clear();
    }
}
