import java.util.ArrayList;

public class Transition {

    /*
     * VARIABLES
     */

    private Integer id;

    // Connected input and output places
    private ArrayList<Place> inputPlaces;
    private ArrayList<Place> outputPlaces;

    // Delay time
    private Integer minDelayTime;
    private Integer maxDelayTime;

    /*
     * CONSTRUCTORS
     */

    public Transition(
            Integer id,
            ArrayList<Place> inputPlaces,
            ArrayList<Place> outputPlaces,
            Integer minDelayTime,
            Integer maxDelayTime) {

        this.id = id;
        this.inputPlaces = inputPlaces;
        this.outputPlaces = outputPlaces;
        this.minDelayTime = minDelayTime;
        this.maxDelayTime = maxDelayTime;
    }

    /*
     * METHODS
     */

    public Boolean fireTransition() {

        // Checks if transition can fire
        Boolean fireable = canFire();

        // Fires transition if possible
        if (fireable) {

            // Token to be rescued from input places
            Token trackedToken = null;
            Token tmpToken;

            // Consumes tokens from input places
            for (int i = 0; i < inputPlaces.size(); i++) {
                tmpToken = inputPlaces.get(i).consume();
                if (tmpToken.getIsTracked()) {
                    trackedToken = tmpToken;
                }
            }

            // Sleeps for a random time between minDelayTime and maxDelayTime
            try {
                Thread.sleep(randomizeDelayTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Produces tokens in output places
            for (int i = 0; i < outputPlaces.size(); i++) {
                outputPlaces.get(i).produce(trackedToken);
            }
        }

        return fireable;
    }

    private Boolean canFire() {

        // Checks if there are enough tokens in input places
        for (int i = 0; i < inputPlaces.size(); i++) {
            if (inputPlaces.get(i).getTokens().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Integer randomizeDelayTime() {
        return (int) (Math.random() * (maxDelayTime - minDelayTime + 1) + minDelayTime);
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() { return id; }

    public ArrayList<Place> getInputPlaces() { return inputPlaces; }

    public ArrayList<Place> getOutputPlaces() { return outputPlaces; }
    
    public Integer getMinDelayTime() { return minDelayTime; }

    public Integer getMaxDelayTime() { return maxDelayTime; }
}
