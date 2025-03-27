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
        if (Monitor.getSimulationState() != 0) {
            transitionFireCounters.set(transition.getId(), transitionFireCounters.get(transition.getId()) + 1);
        }
    }

    public static final void showTokens() {
        System.out.println("=======================|");
        System.out.println(" TOKENS                |");
        System.out.println("=======================|");
        Integer totalTokens = 0;
        for (Token token : PetriNet.getTokens()) {
            System.out.println("Token ID ------------- | " + token.getId());
            System.out.println(" |-----------> Tracked | " + token.getIsTracked());
            totalTokens++;
        }
        System.out.println("Total tokens --------- | " + totalTokens);
    }

    public static final void showPlaces() {
        System.out.println("=======================|");
        System.out.println(" PLACES                |");
        System.out.println("=======================|");
        for (Place place : PetriNet.getPlaces()) {
            System.out.println("Place ID ------------- | " + place.getId());
            System.out.println(" |-----------> Tracked | " + place.getIsTracked());
            System.out.print(" |------------> Tokens | ");
            if (place.getTokens().isEmpty()) {
                System.out.print("None");
            }
            for (Token token : place.getTokens()) {
                System.out.print(token.getId() + " ");
            }
            System.out.println();
        }
    }

    public static final void showTransitions() {
        System.out.println("=======================|");
        System.out.println(" TRANSITIONS           |");
        System.out.println("=======================|");
        for (Transition transition : PetriNet.getTransitions()) {
            System.out.println("Transition ID -------- | " + transition.getId());
            System.out.print(" |------> Input places | ");
            for (Place inputPlace : transition.getInputPlaces()) {
                System.out.print(inputPlace.getId() + " ");
            }
            System.out.print("\n |-----> Output places | ");
            for (Place outputPlace : transition.getOutputPlaces()) {
                System.out.print(outputPlace.getId() + " ");
            }
            System.out.println("\n |----> Min delay time | " + transition.getMinDelayTime());
            System.out.println(" |----> Max delay time | " + transition.getMaxDelayTime());
        }
    }

    public static final void showSegments() {
        System.out.println("=======================|");
        System.out.println(" SEGMENTS              |");
        System.out.println("=======================|");
        for (Segment segment : PetriNet.getSegments()) {
            System.out.println("Segment ID ----------- | " + segment.getId());
            System.out.print(" |------------> Places | ");
            for (Place place : segment.getPlaces()) {
                System.out.print(place.getId() + " ");
            }
            System.out.print("\n |-------> Transitions | ");
            for (Transition transition : segment.getTransitions()) {
                System.out.print(transition.getId() + " ");
            }
            System.out.println("\n |----> Starting place | " + segment.getStartingPlace().getId());
            System.out.println(" |------> Ending place | " + segment.getEndingPlace().getId());
        }
    }

    public static final void showPolicy() {
        System.out.println("=======================|");
        System.out.println(" POLICY                |");
        System.out.println("=======================|");
        for (int i = 0; i < Policy.getProbabilites().length; i++) {
            System.out.println("Probability " + i + " -------- | " + Policy.getProbabilites()[i]);
        }
    }

    public static final synchronized void showTransitionFiring(Transition transition, Boolean isMinimal) {
        if (Monitor.getSimulationState() != 0) {
            System.out.println("=======================|");
            System.out.println(" TRANSITION FIRED      |");
            System.out.println("=======================|");
            System.out.println("Elapsed time --------- | " + (System.currentTimeMillis() - startTime) + " [ms]");
            System.out.println("Transition fired ----- | " + transition.getId());
            System.out.println("Transition counters -- | T0  T1  T2  T3  T4  T5  T6  T7  T8  T9  T10 T11");
            System.out.print("                       | ");
            for (int i = 0; i < transitionFireCounters.size(); i++) {
                System.out.printf("%-4d", transitionFireCounters.get(i));
            }
            System.out.println();
            if (isMinimal) {
                System.out.println("Actual marking ------- | P0  P1  P2  P3  P4  P5  P6  P7  P8  P9  P10 P11 P12 P13 P14 | TOTAL");
                System.out.print("                       | ");
                Integer totalTokens = 0;
                for (Place place : PetriNet.getPlaces()) {
                    System.out.printf("%-4d", place.getTokens().size());
                    totalTokens += place.getTokens().size();
                }
                System.out.printf("| %-4d", totalTokens);
                System.out.println();
            } else {
                for (Place place : PetriNet.getPlaces()) {
                    System.out.println("Place ID ------------- | " + place.getId());
                    System.out.print(" |------------> Tokens | ");
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
    }

    public static final void showStartSimulation(Boolean isMinimal) {
        System.out.println("=======================|");
        System.out.println(" START OF SIMULATION   |");
        System.out.println("=======================|");
        System.out.println("Elapsed time --------- | " + (System.currentTimeMillis() - startTime) + " [ms]");
        System.out.println("Transition counters -- | T0  T1  T2  T3  T4  T5  T6  T7  T8  T9  T10 T11");
        System.out.print("                       | ");
        for (int i = 0; i < transitionFireCounters.size(); i++) {
            System.out.printf("%-4d", transitionFireCounters.get(i));
        }
        System.out.println();
        if (isMinimal) {
            System.out.println("Actual marking ------- | P0  P1  P2  P3  P4  P5  P6  P7  P8  P9  P10 P11 P12 P13 P14 | TOTAL");
            System.out.print("                       | ");
            Integer totalTokens = 0;
            for (Place place : PetriNet.getPlaces()) {
                System.out.printf("%-4d", place.getTokens().size());
                totalTokens += place.getTokens().size();
            }
            System.out.printf("| %-4d", totalTokens);
            System.out.println();
        } else {
            for (Place place : PetriNet.getPlaces()) {
                System.out.println("Place ID ------------- | " + place.getId());
                System.out.print(" |------------> Tokens | ");
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

    public static final void showEndSimulation(Boolean isMinimal) {
        System.out.println("=======================|");
        System.out.println(" END OF SIMULATION     |");
        System.out.println("=======================|");
        System.out.println("Elapsed time --------- | " + (System.currentTimeMillis() - startTime) + " [ms]");
        System.out.println("Transition counters -- | T0  T1  T2  T3  T4  T5  T6  T7  T8  T9  T10 T11");
        System.out.print("                       | ");
        for (int i = 0; i < transitionFireCounters.size(); i++) {
            System.out.printf("%-4d", transitionFireCounters.get(i));
        }
        System.out.println();
        if (isMinimal) {
            System.out.println("Actual marking ------- | P0  P1  P2  P3  P4  P5  P6  P7  P8  P9  P10 P11 P12 P13 P14 | TOTAL");
            System.out.print("                       | ");
            Integer totalTokens = 0;
            for (Place place : PetriNet.getPlaces()) {
                System.out.printf("%-4d", place.getTokens().size());
                totalTokens += place.getTokens().size();
            }
            System.out.printf("| %-4d", totalTokens);
            System.out.println();
        } else {
            for (Place place : PetriNet.getPlaces()) {
                System.out.println("Place ID ------------- | " + place.getId());
                System.out.print(" |------------> Tokens | ");
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

    /*
     * GETTERS AND SETTERS
     */

    public static final Long getStartTime() { return startTime; }

    public static final ArrayList<Integer> getTransitionFireCounters() { return transitionFireCounters; }

    public static final Semaphore getSemaphore() { return semaphore; }
}
