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
    }

    /*
     * GETTERS AND SETTERS
     */

    public void setProbabilites(Integer[] probabilites) {
        this.probabilites = probabilites;
    }
}
