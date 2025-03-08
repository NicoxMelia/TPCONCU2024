public class Place {

    private Integer tokens;
    private Logger logger;

    public Place(
            Integer tokens,
            Logger logger) {
                
        this.tokens = tokens;
        this.logger = logger;
    }

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
