import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Place {

    /*
     * VARIABLES
     */

    private Integer id;
    private Boolean isTracked;
    private ArrayList<Token> tokens;

    // Semaphore to control access to the place
    private Semaphore semaphore;

    /*
     * CONSTRUCTORS
     */

    public Place(
            Integer id,
            Boolean isTracked,
            Integer tokens) {

        this.id = id;
        this.isTracked = isTracked;
        this.tokens = new ArrayList<>();
        for (int i = 0; i < tokens; i++) {
            this.tokens.add(new Token(
                    i + 100 * id,
                    isTracked));
        }
        this.semaphore = new Semaphore(1);
    }

    /*
     * METHODS
     */

    public synchronized Token consume() {
        Token tmp = tokens.get(0);
        tokens.remove(0);
        return tmp;
    }

    public synchronized void produce(Token token) {
        this.tokens.add(token);
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() { return id; }

    public Boolean getIsTracked() { return isTracked; }

    public ArrayList<Token> getTokens() { return tokens; }

    public Semaphore getSemaphore() { return semaphore; }
}
