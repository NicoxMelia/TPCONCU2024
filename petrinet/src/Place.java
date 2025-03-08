public class Place {

    private Integer tokens;

    public Place(Integer tokens) {
        this.tokens = tokens;
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
