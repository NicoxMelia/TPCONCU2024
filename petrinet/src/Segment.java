import java.util.ArrayList;

public class Segment extends Thread {

    private ArrayList<Transition> transitions;
    private ArrayList<Place> places;
    private Place startPlace;
    private Place endPlace;
    private Logger logger;

    public Segment(
            ArrayList<Transition> transitions,
            ArrayList<Place> places,
            Place startPlace,
            Place endPlace,
            Logger logger) {

        this.transitions = transitions;
        this.places = places;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.logger = logger;
    }

    @Override
    public void run() {

        // Fires transitions all the time
        while (true) {
            for (Transition transition : transitions) {
                transition.fireTransition();
            }
        }
    }
}
