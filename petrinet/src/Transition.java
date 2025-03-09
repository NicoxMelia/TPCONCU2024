import java.util.ArrayList;

public class Transition {

    /*
     * VARIABLES
     */

    private Integer id;
    private Integer minDelayTime;
    private Integer maxDelayTime;
    private ArrayList<Integer> consumedQuantities;
    private ArrayList<Integer> producedQuantities;
    private ArrayList<Place> inputPlaces;
    private ArrayList<Place> outputPlaces;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Transition(
            Integer id,
            Integer minDelayTime,
            Integer maxDelayTime,
            ArrayList<Integer> consumedQuantities,
            ArrayList<Integer> producedQuantities,
            ArrayList<Place> inputPlaces,
            ArrayList<Place> outputPlaces,
            Logger logger) {

        this.id = id;        
        this.minDelayTime = minDelayTime;
        this.maxDelayTime = maxDelayTime;
        this.consumedQuantities = consumedQuantities;
        this.producedQuantities = producedQuantities;
        this.inputPlaces = inputPlaces;
        this.outputPlaces = outputPlaces;
        this.logger = logger;
    }

    /*
     * METHODS
     */

    public void fireTransition() {
        if(canFire()) {

            // Consumes tokens from input places
            for (int i = 0; i < inputPlaces.size(); i++) {
                inputPlaces.get(i).consume(consumedQuantities.get(i));
            }

            // Sleeps for a random time between minDelayTime and maxDelayTime
            try {
                Thread.sleep(randomizeDelayTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Produces tokens in output places
            for (int i = 0; i < outputPlaces.size(); i++) {
                outputPlaces.get(i).produce(producedQuantities.get(i));
            }

            // Log
            logger.logTransitionFiring(this);
        }
    }

    public Boolean canFire() {

        // Checks if there are enough tokens in input places
        for (int i = 0; i < inputPlaces.size(); i++) {
            if (inputPlaces.get(i).getTokens() < consumedQuantities.get(i)) {
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

    public Integer getId() {
        return id;
    }
    
    public Integer getMinDelayTime() {
        return minDelayTime;
    }

    public Integer getMaxDelayTime() {
        return maxDelayTime;
    }

    public ArrayList<Integer> getConsumedQuantities() {
        return consumedQuantities;
    }

    public ArrayList<Integer> getProducedQuantities() {
        return producedQuantities;
    }

    public ArrayList<Place> getInputPlaces() {
        return inputPlaces;
    }

    public ArrayList<Place> getOutputPlaces() {
        return outputPlaces;
    }
}
