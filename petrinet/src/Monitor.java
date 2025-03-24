import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Monitor {

    /*
     * CONSTRUCTORS
     */

    private Monitor() {

    }

    /*
     * METHODS
     */

    public static final void start() {
        for (Segment segment : PetriNet.getSegments()) {
            segment.start();
        }
    }

    public static final void updatePolicy(Integer[] probabilities) {
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
                    System.out.println("Semaphore acquire failed.");
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
                System.out.println("Semaphore acquire failed.");
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
}
