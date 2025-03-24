import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Logger {

    /*
     * VARIABLES
     */

    private static Long startTime;
    private static ArrayList<Integer> transitionFireCounters;
    private static Semaphore semaphore;

    /*
     * CONSTRUCTORS
     */

    private Logger() {

    }

    /*
     * METHODS
     */

    public static final void initializeLogger() {
        startTime = System.currentTimeMillis();
        transitionFireCounters = new ArrayList<>();
        for (int i = 0; i < PetriNet.getTransitions().size(); i++) {
            transitionFireCounters.add(0);
        }
        semaphore = new Semaphore(1);
    }

    public static final synchronized void incrementTransitionFireCounter(Transition transition) {
        transitionFireCounters.set(transition.getId(), transitionFireCounters.get(transition.getId()) + 1);
    }

    public static final void showTransitionFireCounters() {
        System.out.println("Fire Counters ------ | T0  T1  T2  T3  T4  T5  T6  T7  T8  T9  T10 T11");
        System.out.print("                     | ");
        for (int i = 0; i < transitionFireCounters.size(); i++) {
            System.out.printf("%-4d", transitionFireCounters.get(i));
        }
        System.out.println();
    }

    public static final synchronized void showActualMarking(Boolean isMinimal) {
        if (isMinimal) {
            System.out.println("Actual marking ----- | P0  P1  P2  P3  P4  P5  P6  P7  P8  P9  P10 P11 P12 P13 P14");
            System.out.print("                     | ");
            for (Place place : PetriNet.getPlaces()) {
                System.out.printf("%-4d", place.getTokens().size());
            }
            System.out.println();
        } else {
            for (Place place : PetriNet.getPlaces()) {
                System.out.println("Place ID: " + place.getId());
                System.out.print(" |--> Tokens: ");
                if (place.getTokens().isEmpty()) {
                    System.out.print("None");
                }
                for (int i = 0; i < place.getTokens().size(); i++) {
                    System.out.print(place.getTokens().get(i).getId() + " ");
                }
                System.out.println();
            }
        }
    }

    public static final synchronized void showTransitionFiring(Transition transition) {
        System.out.println("Transition fired --- | " + transition.getId());
    }

    public static final void showTokensCreation() {
        System.out.println("<< CREATED TOKENS >>");
        for (Token token : PetriNet.getTokens()) {
            System.out.println("Token ID: " + token.getId());
            System.out.println(" |--> Tracked: " + token.getIsTracked());
        }
    }

    public static final void showPlacesCreation() {
        System.out.println("<< CREATED PLACES >>");
        for (Place place : PetriNet.getPlaces()) {
            System.out.println("Place ID: " + place.getId());
            System.out.println(" |--> Tracked: " + place.getIsTracked());
            System.out.print(" |--> Tokens: ");
            if (place.getTokens().isEmpty()) {
                System.out.print("None");
            }
            for (Token token : place.getTokens()) {
                System.out.print(token.getId() + " ");
            }
            System.out.println();
        }
    }

    public static final void showTransitionsCreation() {
        System.out.println("<< CREATED TRANSITIONS >>");
        for (Transition transition : PetriNet.getTransitions()) {
            System.out.println("Transition ID: " + transition.getId());
            System.out.print(" |--> Input places: ");
            for (Place inputPlace : transition.getInputPlaces()) {
                System.out.print(inputPlace.getId() + " ");
            }
            System.out.print("\n |--> Output places: ");
            for (Place outputPlace : transition.getOutputPlaces()) {
                System.out.print(outputPlace.getId() + " ");
            }
            System.out.println("\n |--> Min delay time: " + transition.getMinDelayTime());
            System.out.println(" |--> Max delay time: " + transition.getMaxDelayTime());
        }
    }

    public static final void showSegmentsCreation() {
        System.out.println("<< CREATED SEGMENTS >>");
        for (Segment segment : PetriNet.getSegments()) {
            System.out.println("Segment ID: " + segment.getId());
            System.out.print(" |--> Places: ");
            for (Place place : segment.getPlaces()) {
                System.out.print(place.getId() + " ");
            }
            System.out.print("\n |--> Transitions: ");
            for (Transition transition : segment.getTransitions()) {
                System.out.print(transition.getId() + " ");
            }
            System.out.println("\n |--> Starting place: " + segment.getStartingPlace().getId());
            System.out.println(" |--> Ending place: " + segment.getEndingPlace().getId());
        }
    }

    public static final void showPolicy() {
        for (int i = 0; i < Policy.getProbabilites().length; i++) {
            System.out.println("Probabilities " + i + ": " + Policy.getProbabilites()[i]);
        }
    }

    public static final void showStartTime() {
        System.out.println("Start time reference | " + startTime + " [ms]");
    }

    public static final synchronized void showElapsedTime() {
        System.out.println("Elapsed time ------- | " + (System.currentTimeMillis() - startTime) + " [ms]");
    }

    /*
     * GETTERS AND SETTERS
     */

    public static final Long getStartTime() { return startTime; }

    public static final ArrayList<Integer> getTransitionFireCounters() { return transitionFireCounters; }

    public static final Semaphore getSemaphore() { return semaphore; }
}
