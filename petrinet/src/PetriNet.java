import java.util.ArrayList;

public class PetriNet {

    /*
     * VARIABLES
     */

    private ArrayList<Place> places;
    private ArrayList<Transition> transitions;
    private ArrayList<Segment> segments;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public PetriNet(
            Integer[][] incidenceMatrix,
            Integer[] initialMarking,
            Integer[][] placesSegmentsMatrix,
            Integer[][] transitionsSegmentsMatrix,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes,
            Logger logger) {

        this.places = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.segments = new ArrayList<>();
        this.logger = logger;
        createPlaces(initialMarking);
        createTransitions(incidenceMatrix,
                minDelayTimes,
                maxDelayTimes);
        createSegments(placesSegmentsMatrix,
                transitionsSegmentsMatrix,
                segmentsStarts,
                segmentsEnds);
    }

    /*
     * METHODS
     */

    private void createPlaces(Integer[] initialMarking) {

        // Create places based on initial marking
        for (int i = 0; i < initialMarking.length; i++) {
            Place place = new Place(initialMarking[i],
                    logger);
            this.places.add(place);
        }

        // DEBUG
        if (true) {
            System.out.println("<< CREATED PLACES >>");
            int n = 0;
            for (Place place : places) {
                System.out.println("\nPlace " + n + ":");
                System.out.println("Tokens: " + place.getTokens());
                n++;
            }
            System.out.println();
        }
    }

    private void createTransitions(
            Integer[][] incidenceMatrix,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes) {

        // Create transitions based on incidence matrix columns
        for (int i = 0; i < incidenceMatrix[0].length; i++) {
            ArrayList<Integer> consumedQuantities = new ArrayList<>();
            ArrayList<Integer> producedQuantities = new ArrayList<>();
            ArrayList<Place> inputPlaces = new ArrayList<>();
            ArrayList<Place> outputPlaces = new ArrayList<>();

            // Load consumed & produced quantities and input & output places for each transition based on incidence matrix rows
            for (int j = 0; j < incidenceMatrix.length; j++) {
                if (incidenceMatrix[j][i] < 0) {
                    consumedQuantities.add(-incidenceMatrix[j][i]);
                    inputPlaces.add(places.get(j));
                } else if (incidenceMatrix[j][i] > 0) {
                    producedQuantities.add(incidenceMatrix[j][i]);
                    outputPlaces.add(places.get(j));
                }
            }
            Transition transition = new Transition(
                    minDelayTimes[i],
                    maxDelayTimes[i],
                    consumedQuantities,
                    producedQuantities,
                    inputPlaces,
                    outputPlaces,
                    logger);
            this.transitions.add(transition);
        }

        // DEBUG
        if (true) {
            System.out.println("<< CREATED TRANSITIONS >>");
            int n = 0;
            for (Transition transition : transitions) {
                System.out.println("\nTransition " + n + ":");
                System.out.println("Min delay time: " + transition.getMinDelayTime());
                System.out.println("Max delay time: " + transition.getMaxDelayTime());
                System.out.print("Consumed quantities: ");
                for (Integer consumedQuantity : transition.getConsumedQuantities()) {
                    System.out.print(consumedQuantity + " ");
                }
                System.out.print("\nProduced quantities: ");
                for (Integer producedQuantity : transition.getProducedQuantities()) {
                    System.out.print(producedQuantity + " ");
                }
                System.out.print("\nInput places: ");
                for (Place inputPlace : transition.getInputPlaces()) {
                    System.out.print(places.indexOf(inputPlace) + " ");
                }
                System.out.print("\nOutput places: ");
                for (Place outputPlace : transition.getOutputPlaces()) {
                    System.out.print(places.indexOf(outputPlace) + " ");
                }
                System.out.println();
                n++;
            }
        }
    }

    private void createSegments(
            Integer[][] placesSegmentsMatrix,
            Integer[][] transitionsSegmentsMatrix,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds) {

        // Create transitions and places for each segment based on places segments matrix rows
        for (int i = 0; i < placesSegmentsMatrix.length; i++) {
            ArrayList<Place> places = new ArrayList<>();
            ArrayList<Transition> transitions = new ArrayList<>();
            
            // Load all places of the actual segment based on the places list
            for (int j = 0; j < this.places.size(); j++) {
                if (placesSegmentsMatrix[i][j] == 1) {
                    places.add(this.places.get(j));
                }
            }

            // Load all transitions of the actual segment based on the transitions list
            for (int j = 0; j < this.transitions.size(); j++) {
                if (transitionsSegmentsMatrix[i][j] == 1) {
                    transitions.add(this.transitions.get(j));
                }
            }
            Segment segment = new Segment(
                    places,
                    transitions,
                    places.get(segmentsStarts[i]),
                    places.get(segmentsEnds[i]),
                    logger);
            segments.add(segment);
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public ArrayList<Transition> getTransition() {
        return transitions;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }
}
