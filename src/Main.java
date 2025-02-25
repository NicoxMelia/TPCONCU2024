import Jama.Matrix;

public class Main {
    private static final int cantHilos = 6;
    private static final String policyType = "Equitative";
    //private static final String policyType = "8020";
    private static PetriNet petrinet;               // Petri net representative of the system.
    private static Monitor monitor;                 // Monitor that will control the Petri net that models the system.
    
    //Hilos (Segmentos)
    private static double[] Sa = { 0, 1 };
    private static double[] Sb = { 2, 5 };
    private static double[] Sc = { 3, 4 };
    private static double[] Sd = { 6, 9, 10 };
    private static double[] Se = { 7, 8 };
    private static double[] exit = { 11 };

    // arrays with transitions associated with threads
    private static Matrix SaPath = new Matrix(Sa, 1);       // transposed version of de Sa
    private static Matrix SbPath = new Matrix(Sb, 1);       // transposed version of de Sb
    private static Matrix ScPath = new Matrix(Sc, 1);       // transposed version of de Sc
    private static Matrix SdPath = new Matrix(Sd, 1);       // transposed version of de Sd
    private static Matrix SePath = new Matrix(Se, 1);       // transposed version of de Se
    private static Matrix exitPath = new Matrix(exit, 1);   // transposed version of de exit

    /**
     * Main method.
     *
     * Here the threads with their associated paths are instantiated and executed.
     * Both the Petri net are also initialized with their initial marking
     * like the monitor and the logger thread.
     */

    public static void main(String args[]) {
        Long initTime = System.currentTimeMillis();
        petrinet = new PetriNet();

        Policy policy = new Policy(policyType);
        System.out.println("Policy type: " + policyType + " \n");

        monitor = new Monitor(petrinet, policy);

        Threads[] threads = new Threads[cantHilos
];

        petrinet.enableTransitions();

        try {
            long startTime = System.currentTimeMillis();
            Log log = new Log(petrinet, monitor, startTime, true);
            log.start();

            Log transition = new Log(petrinet, monitor, startTime, false);
            transition.start();

        } catch (Exception e) {
            System.err.println("NO SE PUDO CREAR EL LOG");
            System.exit(1);     // Stop the program with a non-zero exit code
        }

        threads[0] = new Threads(SaPath, monitor, "Sa");
        threads[1] = new Threads(SbPath, monitor, "Sb");
        threads[2] = new Threads(ScPath, monitor, "Sc");
        threads[3] = new Threads(SdPath, monitor, "Sd");
        threads[4] = new Threads(SePath, monitor, "Se");
        threads[5] = new Threads(exitPath, monitor, "Exit");


        for (Threads thread : threads) {
            thread.start();
        }
        try {
            for (Threads thread : threads) {
                thread.join();
            }
        }
        catch(Exception e) {
            System.err.println("❌ I can't wait, I'm tired. ❌");
            System.exit(1);     // Stop the program with a non-zero exit code
        }
        Long finalTime = System.currentTimeMillis();
        //monitor.printDeadThreads();
        System.out.println("\nEnding program!");
        System.out.println(petrinet.transitionsCounterInfo());
        System.out.println("\nT invariants:");
        petrinet.tInvariantsInfo();
        System.out.println("\nElapsed Time: " + (double)((finalTime-initTime)/1000.0) + " seconds");
    }
}
