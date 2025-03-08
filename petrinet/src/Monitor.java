public class Monitor {

    private PetriNet petriNet;
    private Policy policy;
    private Logger logger;

    public Monitor(
            Integer[][] incidenceMatrix,
            Integer[] initialMarking,
            Integer[][] segments,
            Integer[] segmentsStarts,
            Integer[] segmentsEnds,
            Integer[] minDelayTimes,
            Integer[] maxDelayTimes,
            Integer[] probabilities) {
        
        this.logger = new Logger();
        this.petriNet = new PetriNet(
                incidenceMatrix,
                initialMarking,
                segments,
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
