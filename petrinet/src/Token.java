public class Token {

    /*
     * VARIABLES
     */
    
    private Integer id;
    private Boolean isTracked;
    
    /*
     * CONSTRUCTORS
     */

    public Token(
            Integer id,
            Boolean isTracked) {

        this.id = id;
        this.isTracked = isTracked;
    }

    /*
     * GETTERS AND SETTERS
     */

    public Integer getId() { return id; }

    public Boolean getIsTracked() { return isTracked; }
}
