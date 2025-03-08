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
        this.petriNet = new PetriNet(
                incidenceMatrix,
                initialMarking,
                placesSegmentsMatrix,
                transitionsSegmentsMatrix,
                segmentsStarts,
                segmentsEnds,
                minDelayTimes,
                maxDelayTimes,
                logger);
        this.policy = new Policy(
                probabilities,
                logger);
    }    
}
