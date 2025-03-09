import java.util.ArrayList;

public class PetriNet {

    /*
     * VARIABLES
     */

    private ArrayList<Place> places;
    private ArrayList<Transition> transitions;
    private ArrayList<Segment> segments;
    private Policy policy;
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
            Policy policy,
            Logger logger) {

        this.places = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.segments = new ArrayList<>();
        this.policy = policy;
        this.logger = logger;
        createPlaces(initialMarking);
        createTransitions(
                incidenceMatrix,
                minDelayTimes,
                maxDelayTimes);
        createSegments(
                placesSegmentsMatrix,
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
            Place place = new Place(
                    i,
                    initialMarking[i]);
            this.places.add(place);
        }

        // Log
        logger.logPlacesCreation(places);
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
                    i,
                    minDelayTimes[i],
                    maxDelayTimes[i],
                    consumedQuantities,
                    producedQuantities,
                    inputPlaces,
                    outputPlaces);
            this.transitions.add(transition);
        }

        // Log
        logger.logTransitionsCreation(transitions);
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
                    i,
                    places,
                    transitions,
                    this.places.get(segmentsStarts[i]),
                    this.places.get(segmentsEnds[i]),
                    logger);
            segments.add(segment);
        }

        // Log
        logger.logSegmentsCreation(segments);
    }

    /*
     * GETTERS AND SETTERS
     */

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }
}
