import java.util.ArrayList;

public class Transition {

    private Integer minDelayTime;
    private Integer maxDelayTime;
    private ArrayList<Integer> consumedQuantities;
    private ArrayList<Integer> producedQuantities;
    private ArrayList<Place> inputPlaces;
    private ArrayList<Place> outputPlaces;
    private Logger logger;

    public Transition(
            Integer minDelayTime,
            Integer maxDelayTime,
            ArrayList<Integer> consumedQuantities,
            ArrayList<Integer> producedQuantities,
            ArrayList<Place> inputPlaces,
            ArrayList<Place> outputPlaces,
            Logger logger) {
        
        this.minDelayTime = minDelayTime;
        this.maxDelayTime = maxDelayTime;
        this.consumedQuantities = consumedQuantities;
        this.producedQuantities = producedQuantities;
        this.inputPlaces = inputPlaces;
        this.outputPlaces = outputPlaces;
        this.logger = logger;
    }

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
}
