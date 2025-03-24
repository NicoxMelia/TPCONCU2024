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

    /*
     * CONSTRUCTORS
     */

    public Segment(
            long id,
            ArrayList<Place> places,
            ArrayList<Transition> transitions,
            Place startingPlace,
            Place endingPlace) {

        this.id = id;
        this.places = places;
        this.transitions = transitions;
        this.startingPlace = startingPlace;
        this.endingPlace = endingPlace;
    }

    /*
     * METHODS
     */
    
    @Override
    public void run() {

        // Fires possible transitions all the time
        while (true) {
            for (Transition transition : transitions) {
                if (transition.getIsWaiting()) {

                    // Acquires semaphores from input places
                    Monitor.getPlaceSemaphore(transition.getInputPlaces());

                    // Check if transition can fire
                    if (transition.getDelayTime() <= System.currentTimeMillis() && transition.canFire()) {

                        // Acquires semaphores from output places
                        Monitor.getPlaceSemaphore(transition.getOutputPlaces());

                        // Fires transition and logs the firing
                        transition.fireTransition();
                        Monitor.getLoggerSemaphore();
                        Logger.incrementTransitionFireCounter(transition);
                        Logger.showElapsedTime();
                        Logger.showTransitionFiring(transition);
                        Logger.showTransitionFireCounters();
                        Logger.showActualMarking(true);
                        System.out.println();
                        Monitor.releaseLoggerSemaphore();

                        // Releases semaphores from output places
                        Monitor.releasePlaceSemaphore(transition.getOutputPlaces());
                    }

                    // Releases semaphores from input places
                    Monitor.releasePlaceSemaphore(transition.getInputPlaces());
                } else {

                    // Acquires semaphores from input places
                    Monitor.getPlaceSemaphore(transition.getInputPlaces());

                    // Randomizes delay time if transition can fire and set the flag isWaiting to true
                    if (transition.canFire()) {
                        transition.randomizeDelayTime();
                    }

                    // Releases semaphores from input places
                    Monitor.releasePlaceSemaphore(transition.getInputPlaces());
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
