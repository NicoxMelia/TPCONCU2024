import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Monitor implements MonitorInterface {

    /*
     * VARIABLES
     */

    // Indicates the state of the simulation: 0 - stopped, 1 - running
    private static Integer simulationState;

    /*
     * CONSTRUCTORS
     */

    private Monitor() {

    }

    /*
     * METHODS
     */

    public static final void startSimulation() {

        // Show start of simulation
        Logger.showStartSimulation(true);

        // Start simulation
        simulationState = 1;
        for (Segment segment : PetriNet.getSegments()) {
            segment.start();
        }

        // Wait for all segments to finish
        for (Segment segment : PetriNet.getSegments()) {
            try {
                segment.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Show end of simulation
        Logger.showEndSimulation(true);
    }

    @Override
    public final synchronized void fireTransition(Integer transitionId) {
        if (PetriNet.getTransitions().get(transitionId).canFire()) {
            PetriNet.getTransitions().get(transitionId).fireTransition();
        }
    }

    public static final void updatePolicy(Float[] probabilities) {
        Policy.setProbabilites(probabilities);
    }

    public static final void getPlaceSemaphore(ArrayList<Place> places) {
        Boolean areAcquired = false;
        while (!areAcquired) {
            Integer acquired = 0;
            for (Place place : places) {
                try {
                    if (!place.getSemaphore().tryAcquire(5, TimeUnit.MILLISECONDS)) {
                        releasePlaceSemaphore(places, acquired);
                        Thread.sleep((long) Math.random() * 10);
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                acquired++;
                areAcquired = acquired == places.size();
            }
        }
    }

    public static final void getLoggerSemaphore() {
        Boolean isAcquired = false;
        while (!isAcquired) {
            try {
                if (!Logger.getSemaphore().tryAcquire(5, TimeUnit.MILLISECONDS)) {
                    Thread.sleep((long) Math.random() * 10);
                } else {
                    isAcquired = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static final void releasePlaceSemaphore(ArrayList<Place> places) {
        for (Place place : places) {
            place.getSemaphore().release();
        }
    }

    public static final void releasePlaceSemaphore(ArrayList<Place> places, int acquired) {
        for (int i = 0; i < acquired; i++) {
            places.get(i).getSemaphore().release();
        }
    }

    public static final void releaseLoggerSemaphore() {
        Logger.getSemaphore().release();
    }

    /*
     * GETTERS AND SETTERS
     */

    public static final Integer getSimulationState() { return simulationState; }

    public static final void setSimulationState(Integer simulationState) { Monitor.simulationState = simulationState; }
}
