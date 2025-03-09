import java.util.concurrent.Semaphore;

public class Place {

    /*
     * VARIABLES
     */

    private Integer id;
    private Integer tokens;
    private Semaphore semaphore;
    private Integer consumedTokens;
    private Integer producedTokens;

    /*
     * CONSTRUCTORS
     */

    public Place(
            Integer id,
            Integer tokens) {

        this.id = id;
        this.tokens = tokens;
        this.semaphore = new Semaphore(1);
        this.consumedTokens = 0;
        this.producedTokens = 0;
    }

    /*
     * METHODS
     */

    public synchronized void consume(Integer quantity) {
        tokens -= quantity;
        consumedTokens += quantity;
    }

    public synchronized void produce(Integer quantity) {
        tokens += quantity;
        producedTokens += quantity;
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

    public Integer getConsumedTokens() {
        return consumedTokens;
    }

    public Integer getProducedTokens() {
        return producedTokens;
    }
}
