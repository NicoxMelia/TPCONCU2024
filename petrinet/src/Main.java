public class Main {
    
    /*
     * MAIN METHOD
     */
    
    public static void main(String args[]) {
        //CAMBIAR INICIALIZADOR Y METER EN CONSTRUCTOR DE LA CLASE
        PetriNet.initializePetriNet();
        Policy.initializePolicy();
        Logger.initializeLogger();
        Monitor.startSimulation();

        System.out.println("Program successfully finished!");
        return;
    }
}
