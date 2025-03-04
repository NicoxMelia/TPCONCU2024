import java.util.*;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

public class Transitions {

    private final int maxTransitions = 12;
    private ArrayList<Semaphore> conditionTransition; //para la sincronizacion entre hilos, para controlar cada transicion

    public Transitions() {
        conditionTransition = new ArrayList<Semaphore>();
        for (int i = 0; i < maxTransitions; i++) {
            //inicializo cada semaforo con 0 permisos
            conditionTransition.add(new Semaphore(0));
        }
    }


    public ArrayList<Semaphore> getTransitions() {
        return conditionTransition;
    }


    /*
     * Devuelve el índice del primer hilo en cola para la transición asociada con el vector v.
     * Para determinar que semaforo desbloquear cuando un hilo quiere dispara una transicion.
     * 
     * @param v: firing vector
     * @return indice de la primera transicion activa del vector de disparo.
     */

    public int getTransition(Matrix v) {
        int index = 0;

        for (int i = 0; i < v.getColumnDimension(); i++) {
            if (v.get(0, i) == 1)
                break;
            else
                index++;
        }
        return index;
    }

    /*
     * Devuelve el número de subproceso en cola de transiciones.
     * Permite monitorear que trnsiciones estan bloqueadas de forma eficiente
     * 
     * @param 
     * @return matriz de subprocesos en cola.
     */
    public Matrix transitionUp() {
        double[] aux = new double[this.maxTransitions];
        for (Semaphore transition : conditionTransition) { //recorre los semaforos
            if (transition.hasQueuedThreads())
                aux[conditionTransition.indexOf(transition)] = 1; //si hay hilos en cola
            else
                aux[conditionTransition.indexOf(transition)] = 0;
        }
        Matrix waitingThreads = new Matrix(aux, 1);
        return waitingThreads;
    }
}