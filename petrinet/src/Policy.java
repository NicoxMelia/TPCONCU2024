public class Policy {

    /*
     * VARIABLES
     */
    
    private static Float[] probabilites;

    /*
     * CONSTRUCTORS
     */

    private Policy() {

    }

    /*
     * GETTERS AND SETTERS
     */

    public static final Float[] getProbabilites() { return probabilites; }

    public static final void setProbabilites(Float[] probabilites) { Policy.probabilites = probabilites; }
}
