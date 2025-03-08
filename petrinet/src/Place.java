public class Place {

    /*
     * VARIABLES
     */

    private Integer tokens;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Place(
            Integer tokens,
            Logger logger) {

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

    public Integer getTokens() {
        return tokens;
    }
}
