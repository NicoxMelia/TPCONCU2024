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
}
