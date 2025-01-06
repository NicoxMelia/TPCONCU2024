//Elige la primera transición habilitada (FIFO)
import java.util.List;

public class PoliticaBalanceada implements Politica {

    @Override
    public int seleccionarTransicion(List<Integer> transicionesHabilitadas) {
        if (transicionesHabilitadas.isEmpty()) {
            return -1;  // No hay transiciones habilitadas
        }
        return transicionesHabilitadas.get(0);  // Devuelve la primera habilitada (FIFO)
    }
}
