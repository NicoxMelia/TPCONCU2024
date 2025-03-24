public class Monitor {

    /*
     * CONSTRUCTORS
     */

    private Monitor() {

    }

    /*
     * METHODS
     */

    public static final void start() {
        for (Segment segment : PetriNet.getSegments()) {
            segment.start();
        }
    }

    public static final void updatePolicy(Integer[] probabilities) {
        Policy.setProbabilites(probabilities);
    }
}
