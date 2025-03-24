public class Logger {

    /*
     * CONSTRUCTORS
     */

    private Logger() {

    }

    /*
     * METHODS
     */

    public static final synchronized void logActualMarking(Boolean isMinimal) {
        if (isMinimal) {
            System.out.println("P0  P1  P2  P3  P4  P5  P6  P7  P8  P9  P10 P11 P12 P13 P14");
            for (Place place : PetriNet.getPlaces()) {
                System.out.printf("%-4d", place.getTokens().size());
            }
            System.out.println();
        } else {
            System.out.println("<< ACTUAL MARKING >>");
            for (Place place : PetriNet.getPlaces()) {
                System.out.println("Place ID: " + place.getId());
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
    }

    public static final synchronized void logTransitionFiring(Transition transition) {
        System.out.println("Transition ID: " + transition.getId() + " fired.");
    }

    public static void logTokensCreation() {
        System.out.println("<< CREATED TOKENS >>");
        for (Token token : PetriNet.getTokens()) {
            System.out.println("Token ID: " + token.getId());
            System.out.println(" |--> Tracked: " + token.getIsTracked());
        }
    }

    public static final void logPlacesCreation() {
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

    public static final void logTransitionsCreation() {
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

    public static final void logSegmentsCreation() {
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

    public static final void logPolicy() {
        System.out.println("<< POLICY >>");
        for (int i = 0; i < Policy.getProbabilites().length; i++) {
            System.out.println("Probability " + i + ": " + Policy.getProbabilites()[i]);
        }
    }
}
