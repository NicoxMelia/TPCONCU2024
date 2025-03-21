import java.util.ArrayList;

public class PetriNet {

    /*
     * VARIABLES
     */

    private ArrayList<Token> tokens;
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
            Integer[] mainPlaces,
            Integer[] initialMarking,
            Integer[][] placesSegmentsMatrix,
            Integer[][] transitionsSegmentsMatrix,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes,
            Policy policy,
            Logger logger) {

        this.tokens = new ArrayList<>();
        this.places = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.segments = new ArrayList<>();
        this.policy = policy;
        this.logger = logger;
        createTokens(
                mainPlaces,
                initialMarking);
        createPlaces(
                mainPlaces,
                initialMarking);
        createTransitions(
                incidenceMatrix,
                minDelayTimes,
                maxDelayTimes);
        createSegments(
                placesSegmentsMatrix,
                transitionsSegmentsMatrix,
                segmentsStarts,
                segmentsEnds);
        logger.loadPetriNet(
                tokens,
                places,
                transitions,
                segments);
    }

    /*
     * METHODS
     */

    private void createTokens(
            Integer[] mainPlaces,
            Integer[] initialMarking) {

        // Create tokens based on initial marking
        for (int i = 0; i < initialMarking.length; i++) {
            for (int j = 0; j < initialMarking[i]; j++) {
                Token token = new Token(
                        j + 100 * i,
                        mainPlaces[i] == 1);
                this.tokens.add(token);
            }
        }
    }

    private void createPlaces(
            Integer[] mainPlaces,
            Integer[] initialMarking) {

        // Create places based on initial marking
        int tokenIndex = 0;
        for (int j = 0; j < initialMarking.length; j++) {
            ArrayList<Token> tmp = new ArrayList<>();
            for (int k = 0; k < initialMarking[j]; k++) {
                tmp.add(tokens.get(tokenIndex));
                tokenIndex++;
            }
            this.places.add(new Place(
                    j,
                    mainPlaces[j] == 1,
                    tmp));
        }
    }

    private void createTransitions(
            Integer[][] incidenceMatrix,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes) {

        // Create transitions based on incidence matrix columns
        for (int i = 0; i < incidenceMatrix[0].length; i++) {
            ArrayList<Integer> quantitiesToConsume = new ArrayList<>();
            ArrayList<Integer> quantitiesToProduce = new ArrayList<>();
            ArrayList<Place> inputPlaces = new ArrayList<>();
            ArrayList<Place> outputPlaces = new ArrayList<>();

            // Load consumed & produced quantities and input & output places for each transition based on incidence matrix rows
            for (int j = 0; j < incidenceMatrix.length; j++) {
                if (incidenceMatrix[j][i] < 0) {
                    quantitiesToConsume.add(-incidenceMatrix[j][i]);
                    inputPlaces.add(places.get(j));
                } else if (incidenceMatrix[j][i] > 0) {
                    quantitiesToProduce.add(incidenceMatrix[j][i]);
                    outputPlaces.add(places.get(j));
                }
            }
            Transition transition = new Transition(
                    i,
                    inputPlaces,
                    outputPlaces,
                    minDelayTimes[i],
                    maxDelayTimes[i]);
            this.transitions.add(transition);
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
                    i,
                    places,
                    transitions,
                    this.places.get(segmentsStarts[i]),
                    this.places.get(segmentsEnds[i]),
                    logger);
            segments.add(segment);
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    public ArrayList<Place> getPlaces() { return places; }

    public ArrayList<Transition> getTransitions() { return transitions; }

    public ArrayList<Segment> getSegments() { return segments; }

    public Policy getPolicy() { return policy; }

    public Logger getLogger() { return logger; }
}
