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
            Integer[][] segments,
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
        createSegments(segments,
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
    }

    private void createSegments(
            Integer[][] segmentsMatrix,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds) {

        // Create transitions and places for each segment based on segments matrix rows
        for (int i = 0; i < segmentsMatrix.length; i++) {
            ArrayList<Transition> transitions = new ArrayList<>();
            ArrayList<Place> places = new ArrayList<>();
            
            // Load all places on the actual segment based on segments matrix columns
            for (int j = 0; j < segmentsMatrix[i].length; j++) {
                if (segmentsMatrix[i][j] == 1) {
                    places.add(this.places.get(j));
                }
            }

            // Load all transitions on the actual segment
            //xx

            Segment segment = new Segment(
                    transitions,
                    places,
                    places.get(0),
                    places.get(places.size() - 1),
                    logger);
            segments.add(segment);
        }
    }

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
