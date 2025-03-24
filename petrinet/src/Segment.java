import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
            Integer acquired;
            Boolean areAcquired;
            for (Transition transition : transitions) {
                if (transition.getIsWaiting()) {

                    // Acquires semaphores from input places
                    areAcquired = false;
                    while (!areAcquired) {
                        acquired = 0;
                        for (Place place : transition.getInputPlaces()) {
                            try {
                                if (!place.getSemaphore().tryAcquire(5, TimeUnit.MILLISECONDS)) {
                                    for (int i = 0; i < acquired; i++) {
                                        transition.getInputPlaces().get(i).getSemaphore().release();
                                    }
                                    Thread.sleep((long) Math.random() * 10);
                                    break;
                                }
                            } catch (InterruptedException e) {
                                System.out.println("Semaphore acquire failed.");
                            }
                            acquired++;
                            areAcquired = acquired == transition.getInputPlaces().size();
                        }
                    }

                    // Check if transition can fire
                    if (transition.getDelayTime() <= System.currentTimeMillis() && transition.canFire()) {

                        // Acquires semaphores from input places
                        areAcquired = false;
                        while (!areAcquired) {
                            acquired = 0;
                            for (Place place : transition.getOutputPlaces()) {
                                try {
                                    if (!place.getSemaphore().tryAcquire(5, TimeUnit.MILLISECONDS)) {
                                        for (int i = 0; i < acquired; i++) {
                                            transition.getOutputPlaces().get(i).getSemaphore().release();
                                        }
                                        Thread.sleep((long) Math.random() * 10);
                                        break;
                                    }
                                } catch (InterruptedException e) {
                                    System.out.println("Semaphore acquire failed.");
                                }
                                acquired++;
                                areAcquired = acquired == transition.getOutputPlaces().size();
                            }
                        }

                        // Fires transition and logs the firing
                        transition.fireTransition();
                        Logger.logTransitionFiring(transition);
                        Logger.logActualMarking(true);

                        // Releases semaphores from output places
                        for (Place place : transition.getOutputPlaces()) {
                            place.getSemaphore().release();
                        }
                    }

                    // Releases semaphores from input places
                    for (Place place : transition.getInputPlaces()) {
                        place.getSemaphore().release();
                    }
                } else {

                    // Acquires semaphores from input places
                    areAcquired = false;
                    while (!areAcquired) {
                        acquired = 0;
                        for (Place place : transition.getInputPlaces()) {
                            try {
                                if (!place.getSemaphore().tryAcquire(5, TimeUnit.MILLISECONDS)) {
                                    for (int i = 0; i < acquired; i++) {
                                        transition.getInputPlaces().get(i).getSemaphore().release();
                                    }
                                    Thread.sleep((long) Math.random() * 10);
                                    break;
                                }
                            } catch (InterruptedException e) {
                                System.out.println("Semaphore acquire failed.");
                            }
                            acquired++;
                            areAcquired = acquired == transition.getInputPlaces().size();
                        }
                    }

                    // Randomizes delay time if transition can fire and set the flag isWaiting to true
                    if (transition.canFire()) {
                        transition.randomizeDelayTime();
                    }

                    // Releases semaphores from input places
                    for (Place place : transition.getInputPlaces()) {
                        place.getSemaphore().release();
                    }
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
