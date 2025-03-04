/* 
import java.util.ArrayList;
import java.util.List;

public class Monitor implements MonitorInterface {
    private PetriNet petriNet;
    private Politica politica;

    public Monitor(PetriNet petriNet, Politica politica) {
        this.petriNet = petriNet;
        this.politica = politica;
    }

    @Override
    public synchronized boolean fireTransition(int transition) {
        List<Integer> enabledTransitions = getEnabledTransitions();
        if (enabledTransitions.isEmpty()) {
            return false;
        }
        //Usa la politica seleccionada para lanzar transiciones
        int selectedTransition = politica.seleccionarTransicion(enabledTransitions);
        if (selectedTransition != -1 && petriNet.isTransitionEnabled(selectedTransition)) {
            petriNet.executeTransition(selectedTransition);
            System.out.println("Transición " + selectedTransition + " disparada por política.");
            return true;
        }
        return false;
    }

    private List<Integer> getEnabledTransitions() {
        List<Integer> enabled = new ArrayList<>();
        for (int transition : petriNet.getAllTransitions()) {
            if (petriNet.isTransitionEnabled(transition)) {
                enabled.add(transition);
            }
        }
        return enabled;
    }
}
*/

import java.util.HashMap;
import java.util.concurrent.Semaphore;
import Jama.Matrix;

public class Monitor {

    private PetriNet petrinet;
    private Policy policy;
    private Semaphore mutex;
    private Transitions conditionTransition;
    private HashMap<Long, Long> timeLeft;

    private int deadThreads;

    public Monitor(PetriNet petrinet, Policy policy) {
        this.conditionTransition = new Transitions();
        this.petrinet = petrinet;
        this.policy = policy;
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
     * *************************
     * *** PRINCIPAL METHOD  ***
     * *************************
     */

    /*
     * The method checks if the transition is enabled an "time enabled" calling the 'testTime' method.
     * In case all conditions are met, the transition is fired and the method returns true.
     * Otherwise, the thread is queued up and the method returns false.
     *
     * @param v: firing vector
     * @return true if the transition is fired, false otherwise
     */
    public boolean fireTransition(Matrix v) 
    {

        try {
            if (petrinet.getCompletedInvariants() < 200) {
                catchMonitor();
            } else {
                return false;
            }
        } catch(Exception e) {
            System.err.println("❌  I was interrupted with the monitor in my hands  ❌");
            System.exit(1);     // Stop the program with a non-zero exit code
        }


        if(!petrinet.fundamentalEquationTest(v) || (petrinet.workingState(v) == 1)) { // Someone is working on it, but it is not the thread that is requesting it. STATE = OTHER
            exitMonitor();

            int queue = conditionTransition.getTransition(v);

            try {
                conditionTransition.getTransitions().get(queue).acquire();
                if (petrinet.getCompletedInvariants() >= 200)
                        return false;
            } catch(Exception e) {
                System.err.println("❌  current thread is interrupted   ❌");
                System.exit(1);     // Stop the program with a non-zero exit code
            }
        }

        if(testTime(v)) {
            petrinet.fire(v);

            // Checks for threads queued up in sensibilized transitions to wake them up with the established policy.
            Matrix sensibilized = petrinet.getSensibilized();

            Matrix queued = conditionTransition.transitionUp();
            Matrix and = sensibilized.arrayTimes(queued); //  'and' '&'

            int m = result(and); // sensibilized and queued transitions


            if(m > 0) {
                int choice = policy.fireChoice(and);
                // release
                conditionTransition.getTransitions().get(choice).release();
            }else {
                exitMonitor();
            }
            return true;
        } else {
             petrinet.setWorkingVector(v, (double) Thread.currentThread().getId());
             exitMonitor();
             return false;
        }
    }

    /*
     * Test if the transition is "time enabled". Checking if the elapsed time since
     * the sensibilization of the transition is greater than the alpha time.
     * Returns true if the transition is "time enabled", if it's not, returns false and save's the remaining time.
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
