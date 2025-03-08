import java.util.ArrayList;

public class Segment extends Thread {

    private ArrayList<Transition> transitions;
    private ArrayList<Place> places;
    private Place startPlace;
    private Place endPlace;

    public Segment(ArrayList<Transition> transitions,
            ArrayList<Place> places,
            Place startPlace,
            Place endPlace) {

        this.transitions = transitions;
        this.places = places;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    @Override
    public void run() {
        while (true) {
            for (Transition transition : transitions) {
                transition.fireTransition();
            }
        }
    }
}
