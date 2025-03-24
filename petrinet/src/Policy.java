public class Policy {

    /*
     * VARIABLES
     */
    
    private static Integer[] probabilites;

    /*
     * CONSTRUCTORS
     */

    private Policy() {

    }

    /*
     * GETTERS AND SETTERS
     */

    public static final Integer[] getProbabilites() { return probabilites; }

    public static final void setProbabilites(Integer[] probabilites) { Policy.probabilites = probabilites; }
}
