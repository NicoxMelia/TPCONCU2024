import java.util.HashMap;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

public class Monitor implements MonitorInterface{

    private PetriNet petrinet;
    private Politica politica;
    private Semaphore mutex; //Controla el acceso a la sección crítica
    private Transitions conditionTransition;
    private HashMap<Long, Long> timeLeft; //Almacena el tiempo restante para que una transicion sea habilitada

    private int deadThreads; //Cant hilos que han terminado su ejecuccion

    public Monitor(PetriNet petrinet, Politica politica) {
        this.conditionTransition = new Transitions();
        this.petrinet = petrinet;
        this.politica = politica;
        this.timeLeft = new HashMap<Long, Long>();
        this.mutex = new Semaphore(1, true);
        this.deadThreads = 0;
    }

    public Transitions getconditionTransition() {
        return conditionTransition;
    }

    public void addDeadThreads() {
        this.deadThreads++;
    }

    public Semaphore getMutex() {
        return mutex;
    }

    /*
     * El método comprueba si la transición está habilitada en un "tiempo habilitado" llamando al método 'testTime'.
     * En caso de que se cumplan todas las condiciones, la transición se activa y el método devuelve verdadero.
     * De lo contrario, el hilo se pone en cola y el método devuelve falso.
     *
     * @param v: vector de disparo
     * @return verdadero si se activa la transición, falso en caso contrario
     */
    public boolean fireTransition(int transition) {
        boolean qualification = false;
        try {
            mutex.acquire();
            Matrix v = new Matrix(1, petrinet.getIncidenceMatrix().getColumnDimension());
            v.set(0, transition, 1);

            if(testTime(v)) { //verifica si la transicion esta habilitada por tiempo
                petrinet.fire(v); //se dispara si ha esperado el tiempo necesario
                
                // Comprueba si hay hilos en cola en transiciones sensibilizadas para despertarlos con la política establecida.
                Matrix sensibilized = petrinet.getSensibilized(); //Verifica que transiciones estan sensibilizadas
                Matrix queued = conditionTransition.queuedUp(); //Verifica que hilos estan en cola, esperando
                Matrix and = sensibilized.arrayTimes(queued); 

                int m = result(and); // transiciones sensibilizadas y en cola

                if(m > 0) { //si hay hilos esperando
                    int choice = politica.fireChoice(and); //selecciona uno para despertar
                    conditionTransition.getQueued().get(choice).release(); //Despierta el hilo
                } else {
                    exitMonitor(); //libera el mutex
                }
                qualification = true;
            } else {
                //petrinet.setWorkingVector(v, (double) Thread.currentThread().getId()); //Indica que otro hilo esta trabajando en la transicion
                exitMonitor();
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }

        return qualification;    
    }

    /*
     * Prueba si la transición tiene "tiempo habilitado". Comprobando si el tiempo transcurrido desde
     * la sensibilización de la transición es mayor que el tiempo alfa.
     * Devuelve verdadero si la transición tiene "tiempo habilitado", si no lo está, devuelve falso y guarda el tiempo restante.
     * 
     * @param v: vector de disparo
     * @return Verdadero si la transición está "habilitada por tiempo", falso en caso contrario
     */
    private boolean testTime(Matrix v)
    {
        long time = System.currentTimeMillis();
        long alpha = (long) petrinet.getAlphaTimes().get(0, getIndex(v)); //tiempo minimo de espera para disparar la transicion establecidos
        long initTime = (long) petrinet.getSensibilizedTime().get(0, getIndex(v)); //tiempo en que la transicion fue sensibilizada
        if (alpha < (time - initTime) || alpha == 0) { //si ha pasado suficiente tiempo para disparar la transicion o no hay restriccion temporal
            return true;
        } else {
            setTimeLeft(Thread.currentThread().getId(), alpha - (time - initTime)); //Calcula cuánto tiempo falta para que la transición pueda dispararse
            return false;
        }
    }

    /*
     * *************************
     * **** PUBLIC  METHODS ****
     * *************************
     */

    public void printDeadThreads(){
        System.out.println("Dead threads: " + deadThreads + "/12");
    }

    public void catchMonitor() throws InterruptedException {
        mutex.acquire();
    }

    public void exitMonitor() {
        mutex.release();
    }

    /*
     * Devuelve el número de transiciones habilitadas y en cola.
     *
     * @param and: matriz resultante de la operación 'y' entre las transiciones sensibilizadas y en cola.
     * @return número de transiciones habilitadas y en cola.
     */
    public int result(Matrix and){
        int m = 0;

        for (int i = 0; i < and.getColumnDimension(); i++)
            if (and.get(0, i) > 0)
                m++;

        return m;
    }

    /*
     * Devuelve el índice de la transición que se va a activar.
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