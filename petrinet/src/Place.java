import java.util.concurrent.Semaphore;

public class Place {

    /*
     * VARIABLES
     */

    private Integer id;
    private Integer tokens;
    private Semaphore semaphore;

    /*
     * CONSTRUCTORS
     */

    public Place(
            Integer id,
            Integer tokens) {

        this.id = id;
        this.tokens = tokens;
        this.semaphore = new Semaphore(1);
    }

    /*
     * METHODS
     */

    public synchronized void consume(Integer quantity) {
        tokens -= quantity;
    }

    public synchronized void produce(Integer quantity) {
        tokens += quantity;
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() {
        return id;
    }

    public Integer getTokens() {
        return tokens;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
