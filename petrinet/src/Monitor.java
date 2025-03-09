public class Monitor {

    /*
     * VARIABLES
     */

    private PetriNet petriNet;
    private Policy policy;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Monitor(
            Integer[][] incidenceMatrix,
            Integer[] initialMarking,
            Integer[][] placesSegmentsMatrix,
            Integer[][] transitionsSegmentsMatrix,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes,
            Integer[] probabilities) {
        
        this.logger = new Logger();
        this.policy = new Policy(
                probabilities,
                logger);
        this.petriNet = new PetriNet(
                incidenceMatrix,
                initialMarking,
                placesSegmentsMatrix,
                transitionsSegmentsMatrix,
                segmentsStarts,
                segmentsEnds,
                minDelayTimes,
                maxDelayTimes,
                policy,
                logger);
    }

    public void start() {
        for (Segment segment : petriNet.getSegments()) {
            segment.start();
        }
    }

    public void updatePolicy(Integer[] probabilities) {
        policy.setProbabilites(probabilities);
    }
}
