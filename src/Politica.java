import java.util.List;

public interface Politica {
    int seleccionarTransicion(List<Integer> transicionesHabilitadas);
}
