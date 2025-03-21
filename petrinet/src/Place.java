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
            ArrayList<Token> tokens) {

        this.id = id;
        this.isTracked = isTracked;
        this.tokens = tokens;
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
        if (isTracked) {
            tokens.add(token);
        } else {
            Integer tmpId;
            for (int i = 0; true; i++) {
                tmpId = i + 100 * id;
                Boolean isFound = false;
                for (Token t : tokens) {
                    if (t.getId().equals(tmpId)) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    tokens.add(new Token(
                    i + 100 * id,
                    isTracked));
                    break;
                }
            }
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() { return id; }

    public Boolean getIsTracked() { return isTracked; }

    public ArrayList<Token> getTokens() { return tokens; }

    public Semaphore getSemaphore() { return semaphore; }
}
