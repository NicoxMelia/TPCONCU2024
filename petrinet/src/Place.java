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

    public Token consume() {
        Token tmpToken = tokens.get(0);
        tokens.remove(0);
        return tmpToken;
    }

    public void produce(Token token) {
        if (isTracked) {
            tokens.add(token);
        } else {
            Integer tmpTokenId;
            for (int i = 0; true; i++) {
                tmpTokenId = i + 100 * id;
                Boolean isFound = false;
                for (Token t : tokens) {
                    if (t.getId().equals(tmpTokenId)) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    tokens.add(new Token(
                            tmpTokenId,
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
