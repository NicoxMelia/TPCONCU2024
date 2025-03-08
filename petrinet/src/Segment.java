import java.util.ArrayList;

public class Segment extends Thread {

    /*
     * VARIABLES
     */

    private ArrayList<Place> places;
    private ArrayList<Transition> transitions;
    private Place startPlace;
    private Place endPlace;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Segment(
            ArrayList<Place> places,
            ArrayList<Transition> transitions,
            Place startPlace,
            Place endPlace,
            Logger logger) {

        this.places = places;
        this.transitions = transitions;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.logger = logger;
    }

    /*
     * METHODS
     */
    
    @Override
    public void run() {

        // Fires possible transitions all the time
        while (true) {
            for (Transition transition : transitions) {
                transition.fireTransition();
            }
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public Place getEndPlace() {
        return endPlace;
    }
}
