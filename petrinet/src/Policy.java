public class Policy {

    private Integer[] probabilites;
    private Logger logger;

    public Policy(
            Integer[] probabilites,
            Logger logger) {

        this.probabilites = probabilites;
    }
}
