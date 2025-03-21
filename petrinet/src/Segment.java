import java.util.ArrayList;

public class Segment extends Thread {

    /*
     * VARIABLES
     */

    private long id;
    private ArrayList<Place> places;
    private ArrayList<Transition> transitions;
    private Place startingPlace;
    private Place endingPlace;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Segment(
            long id,
            ArrayList<Place> places,
            ArrayList<Transition> transitions,
            Place startingPlace,
            Place endingPlace,
            Logger logger) {

        this.id = id;
        this.places = places;
        this.transitions = transitions;
        this.startingPlace = startingPlace;
        this.endingPlace = endingPlace;
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

                // Acquires semaphores from input places
                for (Place place : transition.getInputPlaces()) {
                    try {
                        place.getSemaphore().acquire();
                    } catch (InterruptedException e) {
                        System.out.println("ERROR: Place ID " + place.getId() + " semaphore acquire failed.");
                    }
                }

                // Aquires semaphores from output places
                for (Place place : transition.getOutputPlaces()) {
                    try {
                        place.getSemaphore().acquire();
                    } catch (InterruptedException e) {
                        System.out.println("ERROR: Place ID " + place.getId() + " semaphore acquire failed.");
                    }
                }

                // Fires transition if possible and then log
                if (transition.fireTransition()) {

                    // Log
                    logger.logTransitionFiring(transition);
                }

                // Releases semaphores from input places
                for (Place place : transition.getInputPlaces()) {
                    place.getSemaphore().release();
                }

                // Releases semaphores from output places
                for (Place place : transition.getOutputPlaces()) {
                    place.getSemaphore().release();
                }
            }
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    public long getId() { return id; }

    public ArrayList<Place> getPlaces() { return places; }

    public ArrayList<Transition> getTransitions() { return transitions; }

    public Place getStartingPlace() { return startingPlace; }

    public Place getEndingPlace() { return endingPlace; }
}
