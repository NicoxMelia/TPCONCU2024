//Prioriza transiciones en un orden definido (el primero en la lista)
public class PoliticaPriorizada extends Politica {
    @Override
    public int seleccionarTransicion(List<Integer> transiciones) {
        return transiciones.stream().findFirst().orElse(-1);
    }
}

//ME DIO ESTA OTRA IMPLEMENTACION, PERO CREO Q ES MAS COMPLEJA
//SI NO MAL ENTIENDO LA QUE YA TENEMOS, DISPARA LAS TRANSICIONES NI BIEN ESTEN DISPONIBLES

//Para crear desde el main
//  Politica politicaPriorizada = new PoliticaPriorizada(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
/*
 import java.util.List;

public class PoliticaPriorizada implements Politica {
    private final List<Integer> ordenPrioridad;

    public PoliticaPriorizada(List<Integer> ordenPrioridad) {
        this.ordenPrioridad = ordenPrioridad;
    }

    @Override
    public int seleccionarTransicion(List<Integer> transicionesHabilitadas) {
        for (int transicionPrioritaria : ordenPrioridad) {
            if (transicionesHabilitadas.contains(transicionPrioritaria)) {
                return transicionPrioritaria;  // Devuelve la primera transición según el orden de prioridad
            }
        }
        return -1;  // No hay transiciones habilitadas con prioridad
    }
}
 
 */