import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
public class Policy {

    /*
     * VARIABLES
     */
    
    private static int policyId; // ID = 0 -> Policy balanceada, ID = 1 -> Policy priorizada.
    private static HashMap<Integer, Integer> transitionFireCounters;
    /*
     * CONSTRUCTORS
     */

    private Policy() {

    }

    /*
     * METHODS
     */

    public static final void initializePolicy() {
        System.out.println("0 -> politica balanceada");
        System.out.println("1 -> politica priorizada");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione la política a usar: ");
        policyId = scanner.nextInt();
        if (policyId != 0 && policyId != 1) {
            System.out.println("Selección inválida. Se usará la política por defecto (balanceada).");
            policyId = 0;
        }
        scanner.close();
        //Contador de disparo de transiciones (YA ESTA IMPLEMENTADO PERO NO SE COMO)
        Policy.transitionFireCounters = new HashMap<>();
        for (int i = 0; i < PetriNet.getTransitions().size(); i++) {
            transitionFireCounters.put(i, 0);
        }
        Logger.showPolicy();
    }

    public static final boolean shouldFireTransition(int transitionId) {
        //Pregunto por que politica esta seleccionada
        if(getPolicyId() == 0){ //Politica balanceada
            if (transitionId == 2 || transitionId == 3) {
                int countT2 = transitionFireCounters.get(2);
                int countT3 = transitionFireCounters.get(3);
                if (transitionId == 2 && countT2 <= countT3) {
                    return true;
                } else if (transitionId == 3 && countT3 <= countT2) {
                    return true;
                }
                return false;
            }
            if (transitionId == 6 || transitionId == 7) {
                int countT6 = transitionFireCounters.get(6);
                int countT7 = transitionFireCounters.get(7);
                if (transitionId == 6 && countT6 <= countT7) {
                    return true;
                } else if (transitionId == 7 && countT7 <= countT6) {
                    return true;
                }
                return false;
            }
    
            // Para otras transiciones, ejecuta siempre
            return true;

        }
        else{ //Politica priorizada
            if (transitionId == 2 || transitionId == 3) {
            int countT2 = transitionFireCounters.get(2);
            int countT3 = transitionFireCounters.get(3);
            Random random = new Random();
            double probability = random.nextDouble();

            if (transitionId == 2 && (probability < 0.75 || countT2 <= countT3)) {
                return true;
            } else if (transitionId == 3 && (probability >= 0.75 && countT3 < countT2)) {
                return true;
            }
            return false;
        } else if (transitionId == 6 || transitionId == 7) {
            int countT6 = transitionFireCounters.get(6);
            int countT7 = transitionFireCounters.get(7);
            Random random = new Random();
            double probability = random.nextDouble();

            if (transitionId == 6 && (probability < 0.80 || countT6 <= countT7)) {
                return true;
            } else if (transitionId == 7 && (probability >= 0.80 && countT7 < countT6)) {
                return true;
            }
            return false;
        }
            //Si viene cualquier otra transicion, disparo
            else{
                return true;
            }
        }
    }

    public static final void incrementTransitionCounter(int transitionId) {
        transitionFireCounters.put(transitionId, transitionFireCounters.get(transitionId) + 1);
    }

    /*
     * GETTERS AND SETTERS
     */

    public static final int getPolicyId() { return policyId; }

    public static final void setPolicyId(int policy) { Policy.policyId = policy; }
}
