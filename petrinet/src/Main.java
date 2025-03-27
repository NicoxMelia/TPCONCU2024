import java.util.Scanner;

public class Main {
    
    /*
     * MAIN METHOD
     */
    
    public static void main(String args[]) {

        // Initialize the PetriNet, policy and logger
        PetriNet.initializePetriNet();
        Policy.initializePolicy();
        Logger.initializeLogger();
        
        // Selection of simulation or manual mode and run
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Select mode (0 - simulation, 1 - manual mode): ");
            String input = scanner.nextLine();
            if (input.equals("0")) {
                Monitor.startSimulation();
                break;
            } else if (input.equals("1")) {
                Monitor.startManualMode();
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
        scanner.close();

        // Show end of manual mode
        System.out.println("Program successfully finished!");
        return;
    }
}
