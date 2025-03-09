public class Policy {

    /*
     * VARIABLES
     */
    
    private Integer[] probabilites;
    private Logger logger;

    /*
     * CONSTRUCTORS
     */

    public Policy(
            Integer[] probabilites,
            Logger logger) {

        this.probabilites = probabilites;
        this.logger = logger;

        // Log
        logger.logPolicy(probabilites);
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer[] getProbabilites() {
        return probabilites;
    }

    public void setProbabilites(Integer[] probabilites) {
        this.probabilites = probabilites;

        // Log
        logger.logPolicy(probabilites);
    }
}
