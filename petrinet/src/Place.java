public class Place {

    /*
     * VARIABLES
     */

    private Integer id;
    private Integer tokens;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Place(
            Integer id,
            Integer tokens,
            Logger logger) {

        this.id = id;
        this.tokens = tokens;
        this.logger = logger;
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
}
