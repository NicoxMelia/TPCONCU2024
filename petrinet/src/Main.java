public class Main {
    
    /*
     * MAIN METHOD
     */
    
    public static void main(String args[]) {
        PetriNet.initializePetriNet();
        Policy.initializePolicy();
        Logger.initializeLogger();
        Monitor.startSimulation();

        System.out.println("Program successfully finished!");
        return;
    }
}
