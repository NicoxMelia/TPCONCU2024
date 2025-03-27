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
        
        // Selection and start of simulation or manual mode
        System.out.print("Select mode (0 - simulation, 1 - manual mode): ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("0")) {
                Monitor.startSimulation();
                break;
            } else if (input.equals("1")) {
                Monitor.startManualMode();
                break;
            } else {
                System.out.println("Invalid input.");
                System.out.print("Select mode (0 - simulation, 1 - manual mode): ");
            }
        }
        scanner.close();

        // Show end of manual mode
        System.out.println("Program successfully finished!");
        return;
    }
}
