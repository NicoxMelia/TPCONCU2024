import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetriNet {
    private Map<Integer, Integer> places;
    private Map<Integer, List<Integer>> inputTransitions;
    private Map<Integer, List<Integer>> outputTransitions;

    public PetriNet() {
        places = new HashMap<>();
        inputTransitions = new HashMap<>();
        outputTransitions = new HashMap<>();
        initialize();
    }

    public void initialize() {
        // Inicializar plazas y tokens
        places.put(0, 5);  // P0 tiene 5 tokens
        places.put(1, 1);  // P1 tiene 1 token
        places.put(4, 5);  // P4 tiene 5 tokens
        places.put(6, 1);  // P6 tiene 1 token
        places.put(10, 1); // P10 tiene 1 token

        // Definir transiciones y plazas de entrada/salida
        //Transicion 0
        inputTransitions.put(0, List.of(0,1,4));
        outputTransitions.put(0, List.of(2));
        //Transicion 1
        inputTransitions.put(1, List.of(2));
        outputTransitions.put(1, List.of(3,1));
        //Transicion 2
        inputTransitions.put(2, List.of(3,6));
        outputTransitions.put(2, List.of(4,5));
        //Transicion 3
        inputTransitions.put(3, List.of(3,7));
        outputTransitions.put(3, List.of(4,8));
        //Transicion 4
        inputTransitions.put(4, List.of(8));
        outputTransitions.put(4, List.of(7,9));
        //Transicion 5
        inputTransitions.put(5, List.of(5));
        outputTransitions.put(5, List.of(6,9));
        //Transicion 6
        inputTransitions.put(6, List.of(9,10));
        outputTransitions.put(6, List.of(11));
        //Transicion 7
        inputTransitions.put(7, List.of(9,10));
        outputTransitions.put(7, List.of(12));
        //Transicion 8
        inputTransitions.put(8, List.of(12));
        outputTransitions.put(8, List.of(10,14));
        //Transicion 9
        inputTransitions.put(9, List.of(11));
        outputTransitions.put(9, List.of(13));
        //Transicion 10
        inputTransitions.put(10, List.of(13));
        outputTransitions.put(10, List.of(10,14));
        //Transicion 11
        inputTransitions.put(11, List.of(14));
        outputTransitions.put(11, List.of(0)); // Cierra el ciclo
    }

    public boolean isTransitionEnabled(int transition) {
        List<Integer> requiredPlaces = inputTransitions.get(transition);
        for (int place : requiredPlaces) {
            if (places.get(place) <= 0) return false;
        }
        return true;
    }

    public void executeTransition(int transition) {
        List<Integer> requiredPlaces = inputTransitions.get(transition);
        List<Integer> outputPlaces = outputTransitions.get(transition);

        for (int place : requiredPlaces) {
            places.put(place, places.get(place) - 1);
        }
        for (int place : outputPlaces) {
            places.put(place, places.getOrDefault(place, 0) + 1);
        }
    }

    public List<Integer> getAllTransitions() {
        return new ArrayList<>(inputTransitions.keySet());
    }

}