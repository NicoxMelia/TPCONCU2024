import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

public class Monitor {
    //Controla el acceso a la RdP usando un mutex para asegurar que solo un hilo a la vez pueda acceder

    private PetriNet petrinet;
    private Policy policy;
    private Semaphore mutex; //(binario) para asegurar que un hilo a la vez acceda a la seccion critica
    private Transitions conditionTransition; //para bloquear hilos que no pueden disparar una transicion
    private ConcurrentHashMap<Long, Long> timeLeft; //tiempo restante para que un hilo pueda disparar una transicion

    private int deadThreads;

    public Monitor(PetriNet petrinet, Policy policy) {
        this.conditionTransition = new Transitions();
        this.petrinet = petrinet;
        this.policy = policy;
        this.timeLeft = new ConcurrentHashMap<>();
        this.mutex = new Semaphore(1, true); //prioriza hilos en orden FIFO
        this.deadThreads = 0;
    }

    public Transitions getconditionTransition() {
        return conditionTransition;
    }

    public synchronized void addDeadThreads() {
        this.deadThreads++;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    /*
     * *************************
     * *** PRINCIPAL METHOD  ***
     * *************************
     */

    /*
     * El método verifica si la transición está habilitada en un "tiempo habilitado" llamando al método 'testTime'.
     * En caso de que se cumplan todas las condiciones, la transición se activa y el método devuelve verdadero.
     * De lo contrario, el hilo se pone en cola y el método devuelve falso.
     *
     * @param v: firing vector
     * @return true if the transition is fired, false otherwise
     */
    public boolean fireTransition(Matrix v) 
    {

        try {
            if (petrinet.getCompletedInvariants() < 200) {
                catchMonitor(); //para bloquear el acceso con el monitor
            } else {
                return false;
            }
        } catch(Exception e) {
            System.err.println("❌  I was interrupted with the monitor in my hands  ❌");
            System.exit(1);     // Stop the program with a non-zero exit code
        }

        //a traves de la ec fundamental, verifica si una transicion no puede dispararse o si ya hay otro hilo trabajandola
        if(!petrinet.fundamentalEquationTest(v) || (petrinet.workingState(v) == 1)) { 
            exitMonitor();

            int queue = conditionTransition.getTransition(v);
            
            //Si un hilo es interrupido mientras sostiene el monitos, el programa termina
            //en lugar de dejar el sistema bloqueado en un estado incorrecto.
            try {
                //obtiene el semaforo correspondiente a la transicion queue y pone en espera al hilo hasta que otro lo despierte
                conditionTransition.getTransitions().get(queue).acquire();
                if (petrinet.getCompletedInvariants() >= 200)
                        return false;
            } catch(Exception e) {
                System.err.println("❌  current thread is interrupted   ❌");
                System.exit(1);
            }
        }

        if(testTime(v)) {
            petrinet.fire(v);

            // Checks for threads queued up in sensibilized transitions to wake them up with the established policy.
            Matrix sensibilized = petrinet.getSensibilized();

            Matrix queued = conditionTransition.transitionUp();
            Matrix and = sensibilized.arrayTimes(queued);

            int m = result(and); // sensibilized and queued transitions


            if(m > 0) {
                int choice = policy.fireChoice(and);
                // release
                conditionTransition.getTransitions().get(choice).release();
            }else {
                exitMonitor();
            }
            return true;
        } else { //se marca como en espera
             petrinet.setWorkingVector(v, (double) Thread.currentThread().getId());
             exitMonitor();
             return false;
        }
    }

    /*
     * Pruebe si la transición tiene "tiempo habilitado". Comprobando si el tiempo transcurrido desde
     * la sensibilización de la transición es mayor que el tiempo alfa.
     * Devuelve verdadero si la transición tiene "tiempo habilitado", si no lo está, devuelve falso y guarda el tiempo restante.
     *
     * @param v: firing vector
     * @return true if the transition is "time enabled", false otherwise
     */
    private boolean testTime(Matrix v)
    {
        long time = System.currentTimeMillis();
        long alpha = (long) petrinet.getAlphaTimes().get(0, getIndex(v));
        long initTime = (long) petrinet.getSensibilizedTime().get(0, getIndex(v));
        if (alpha < (time - initTime) || alpha == 0) {
            return true;
        } else {
            setTimeLeft(Thread.currentThread().getId(), alpha - (time - initTime));
            return false;
        }
    }

    /*
     * *************************
     * **** PUBLIC  METHODS ****
     * *************************
     */

    public void printDeadThreads()
    {
        System.out.println("Dead threads: " + deadThreads + "/12");
    }

    public void catchMonitor() throws InterruptedException {
        mutex.acquire();
    }

    public void exitMonitor() {
        mutex.release();
    }

    /*
     * Returns the number of enabled and queued transitions.
     *
     * @param and: matrix resulting from the 'and' operation between the sensibilized and queued transitions.
     * @return the number of enabled and queued transitions.
     */
    public int result(Matrix and)
    {
        int m = 0;

        for (int i = 0; i < and.getColumnDimension(); i++)
            if (and.get(0, i) > 0)
                m++;

        return m;
    }

    /*
     * Returns the index of the transition that is going to be fired.
     *
     * @param v: firing vector
     * @return index of the transition
     */
    private int getIndex(Matrix vector) {
        int index = 0;

        for (int i = 0; i < vector.getColumnDimension(); i++) {
            if (vector.get(0, i) == 1)
                break;
            else
                index++;
        }

        return index;
    }

    /*
     * *************************
     * *** Getters & Setters ***
     * *************************
     */

    public synchronized long getTimeLeft(long id) {
        return this.timeLeft.get(id);
    }

    private synchronized void setTimeLeft(long id, long time) {
        timeLeft.put(id, time);
    }

    public PetriNet getPetriNet() {
        return this.petrinet;
    }
}