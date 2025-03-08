import java.util.ArrayList;

public class PetriNet {

    private ArrayList<Place> places;
    private ArrayList<Transition> transitions;
    private ArrayList<Segment> segments;

    public PetriNet(Integer[][] incidenceMatrix,
            Integer[] initialMarking,
            Integer[][] segments,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes) {

        this.places = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.segments = new ArrayList<>();
        createPlaces(initialMarking);
        createTransitions(incidenceMatrix, minDelayTimes, maxDelayTimes);
        createSegments(segments, segmentsStarts, segmentsEnds);
    }

    private void createPlaces(Integer[] initialMarking) {

        // Create places
        for (int i = 0; i < initialMarking.length; i++) {
            Place place = new Place(initialMarking[i]);
            this.places.add(place);
        }
        
    }

    private void createTransitions(Integer[][] incidenceMatrix,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes) {

        // Create transitions
        for (int i = 0; i < incidenceMatrix[0].length; i++) {
            ArrayList<Integer> consumedQuantities = new ArrayList<>();
            ArrayList<Integer> producedQuantities = new ArrayList<>();
            ArrayList<Place> inputPlaces = new ArrayList<>();
            ArrayList<Place> outputPlaces = new ArrayList<>();
            for (int j = 0; j < incidenceMatrix.length; j++) {
                if (incidenceMatrix[j][i] < 0) {
                    consumedQuantities.add(-incidenceMatrix[j][i]);
                    inputPlaces.add(places.get(j));
                } else if (incidenceMatrix[j][i] > 0) {
                    producedQuantities.add(incidenceMatrix[j][i]);
                    outputPlaces.add(places.get(j));
                }
            }
            Transition transition = new Transition(minDelayTimes[i],
                    maxDelayTimes[i],
                    consumedQuantities,
                    producedQuantities,
                    inputPlaces,
                    outputPlaces);
            this.transitions.add(transition);
        }
    }

    private void createSegments(Integer[][] segmentsMatrix,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds) {

        // Create segments
        for (int i = 0; i < segmentsMatrix.length; i++) {
            ArrayList<Transition> transitions = new ArrayList<>();
            ArrayList<Place> places = new ArrayList<>();
            for (int j = 0; j < segmentsMatrix[i].length; j++) {
                if (segmentsMatrix[i][j] == 1) {
                    places.add(this.places.get(j));
                }
            }
            

            // POSSIBLE ERROR HERE
            for (int j = segmentsStarts[i]; j < segmentsEnds[i]; j++) {
                places.add(this.places.get(j));
            }
            Segment segment = new Segment(transitions, places, places.get(0), places.get(places.size() - 1));
            this.segments.add(segment);
        }
    }

    public void start() {
        for (Segment segment : segments) {
            segment.start();
        }
    }
}
