import java.util.ArrayList;

public class Transition {

    private Integer minDelayTime;
    private Integer maxDelayTime;
    private ArrayList<Integer> consumedQuantities;
    private ArrayList<Integer> producedQuantities;
    private ArrayList<Place> inputPlaces;
    private ArrayList<Place> outputPlaces;

    public Transition(Integer minDelayTime,
            Integer maxDelayTime,
            ArrayList<Integer> consumedQuantities,
            ArrayList<Integer> producedQuantities,
            ArrayList<Place> inputPlaces,
            ArrayList<Place> outputPlaces) {
        
        this.inputPlaces = inputPlaces;
        this.outputPlaces = outputPlaces;
    }

    public void fireTransition() {
        if(canFire()) {
            for (int i = 0; i < inputPlaces.size(); i++) {
                inputPlaces.get(i).consume(consumedQuantities.get(i));
            }
            try {
                Thread.sleep(randomizeDelayTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < outputPlaces.size(); i++) {
                outputPlaces.get(i).produce(producedQuantities.get(i));
            }
        }
    }

    public Boolean canFire() {
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
