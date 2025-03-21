import java.util.ArrayList;

public class Logger extends Thread {
    
    /*
     * CONSTRUCTORS
     */

    public Logger() {

    }

    /*
     * METHODS
     */

    @Override
    public void run() {

    }

    public void logTransitionFiring(Transition transition) {
        System.out.println("Transition ID: " + transition.getId() + " fired.");
    }

    public void logPlacesCreation(ArrayList<Place> places) {
        System.out.println("<< CREATED PLACES >>");
        for (Place place : places) {
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

    public void logTransitionsCreation(ArrayList<Transition> transitions) {
        System.out.println("<< CREATED TRANSITIONS >>");
        for (Transition transition : transitions) {
            System.out.println("Transition ID: " + transition.getId());
            System.out.println(" |--> Min delay time: " + transition.getMinDelayTime());
            System.out.println(" |--> Max delay time: " + transition.getMaxDelayTime());
            System.out.print("\n |--> Input places: ");
            for (Place inputPlace : transition.getInputPlaces()) {
                System.out.print(inputPlace.getId() + " ");
            }
            System.out.print("\n |--> Output places: ");
            for (Place outputPlace : transition.getOutputPlaces()) {
                System.out.print(outputPlace.getId() + " ");
            }
            System.out.println();
        }
    }

    public void logSegmentsCreation(ArrayList<Segment> segments) {
        System.out.println("<< CREATED SEGMENTS >>");
        for (Segment segment : segments) {
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

    public void logPolicy(Integer[] policy) {
        System.out.println("<< POLICY >>");
        for (int i = 0; i < policy.length; i++) {
            System.out.println("Probability " + i + ": " + policy[i]);
        }
    }
}
