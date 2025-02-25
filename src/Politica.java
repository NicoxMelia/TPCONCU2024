import Jama.Matrix;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class Politica {
    private String policyType;
    private ArrayList<Integer> transitions;
    private Random randomGenerator;

    public Politica(String policyType) {
        this.policyType = policyType;
        this.transitions = new ArrayList<>();
        randomGenerator = new Random();
    }

    /*
     * Fork 1: T2 T3
     * Fork 2: T6 T7
     */
    public int fireChoice(Matrix matrix) {
        int indexChosen = 0;
        if (policyType.equals("7580")) {
            if (matrix.get(0, 2) >= 1 || matrix.get(0, 3) >= 1) { // if transitions 2 or 3 are enabled
                double probT2 = 0.75;
                double randomNum = randomGenerator.nextDouble();
                if (randomNum < probT2) {
                    indexChosen = 2; // Chose index 2
                    System.out.println("Firing T2");
                } else {
                    indexChosen = 3; // Chose index 3
                    System.out.println("Firing T3");
                }
            } else if (matrix.get(0, 6) >= 1 || matrix.get(0, 7) >= 1) { // if transitions 6 or 7 are enabled
                double probT6 = 0.8;
                double randomNum = randomGenerator.nextDouble();
                if (randomNum < probT6) {
                    indexChosen = 6; // Chose index 6
                    System.out.println("Firing T6");
                } else {
                    indexChosen = 7; // Chose index 7
                    System.out.println("Firing T7");
                }
            } else {
                // If the sensibilized transitions are not T2, T3, T6, or T7, chooses randomly
                transitions.clear(); // clear array
                for (int i = 0; i < matrix.getColumnDimension(); i++) {
                    if (matrix.get(0, i) > 0) {
                        transitions.add(i);
                    }
                }
                int choice = randomGenerator.nextInt(transitions.size());
                indexChosen = transitions.get(choice);
            }
        } else if (this.policyType == "Equitative") { 
            // Si el tipo de política es equitativa, elegir aleatoriamente con una distribución normal de probabilidades            transitions.clear();
            for (int i = 0; i < matrix.getColumnDimension(); i++) {
                if (matrix.get(0, i) > 0) {
                    transitions.add(i);
                }
            }
            int choice = (int) Math.round(randomGenerator.nextInt(transitions.size()));
            indexChosen = (int) Math.round(transitions.get(choice));
        }
        return indexChosen;
    }
}
