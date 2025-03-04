import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class PetriNet {

    private int cantIT = 4;
    private int cantTransiciones = 12;

    private Matrix Incidencia;
    private Matrix inversaIncidencia;
    private Matrix marcaActual;
    private Matrix vecTsensibilizadas; // vector de transiciones sensibilizadas
    private Matrix matriz;
    private Matrix workingVector;
    private Matrix alphaTime;
    private Matrix countT;
    private Matrix sensibilizedTime;
    public ArrayList<Integer> tInvariantsAux;
    private ArrayList<String> firedSequence;
    private int[] countIT;
    private static int completedInvariants;
    private String rutaActual;
    private String allTransitionsPrint;

    private final double[][] matrixIndicence = {
        //T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11
        { -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },    //P0
        { -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P1
        { 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P2
        { 0, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0 },   //P3
        { -1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },    //P4
        { 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0 },    //P5
        { 0, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 0 },    //P6
        { 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 0, 0 },    //P7
        { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0 },    //P8
        { 0, 0, 0, 0, 0, 1, 1, -1, -1, 0, 0, 0,},   //P9
        { 0, 0, 0, 0, 0, 0, -1, -1, 1, 0, 1, 0 },   //P10
        { 0, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0 },    //P11
        { 0, 0, 0, 0, 0, 0, 0, 1, -1, 0, 0, 0 },    //P12
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1, 0 },    //P13
        { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, -1 }     //P14 
    };

    // private final double[][] tInvariant = {
    //         { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1 }, // T1 T3 T5 T7 T9 T11 T12 T13
    //         { 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 }, // T1 T3 T5 T7 T8 T10 T12 T13
    //         { 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1 }, // T1 T3 T4 T6 T9 T11 T12 T13
    //         { 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 }, // T1 T3 T4 T6 T8 T10 T12 T13
    //         { 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1 }, // T0 T2 T5 T7 T9 T11 T12 T13
    //         { 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1 }, // T0 T2 T5 T7 T8 T10 T12 T13
    //         { 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1 }, // T0 T2 T4 T6 T9 T11 T12 T13
    //         { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 }, // T0 T2 T4 T6 T8 T10 T12 T13
    // };
    // private final double[][] pInvariant = {
    //         // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
    //         { 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 1 0,3,5,6,9,11,12,13,15,16,17
    //         { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 2 1,3,
    //         { 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 3 2,5
    //         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 4 13,14,15
    //         { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 5 7,9
    //         { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // 6 8,11------------------
    //         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // 7 9,10,11
    //         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }, // 8 17,18----------------
    //         { 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // 9 3,4,5,17
    // };

    private final double[][] bIncidencia = {
        //T0 T1 T2 T3 T4 T5 T6 T7 T8 T9 T10 T11
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P0
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P1
        { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P2
        { 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },   //P3
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P4
        { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },    //P5
        { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },    //P6
        { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },    //P7
        { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },    //P8
        { 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0,},    //P9
        { 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },    //P10
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },    //P11
        { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },    //P12
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },    //P13
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }     //P14  
    };
                                           // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14
    private final double[] initialMarking = { 1, 1, 0, 0, 5, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0};

                                   // 0   1   2  3   4    5   6  7   8    9   10   11 
    private final double[] aTimes = { 0, 200, 0, 0, 100, 100, 0, 0, 200, 100, 100, 0 };
    public List<Integer> tInvariantSum;

    public PetriNet()
    {
        this.rutaActual = "";
        //this.completedInvariants = 0;
        this.countIT = new int[cantIT];
        this.Incidencia = new Matrix(matrixIndicence);
        this.inversaIncidencia = new Matrix(bIncidencia);
        this.marcaActual = new Matrix(initialMarking, 1);
        this.vecTsensibilizadas = new Matrix(1, Incidencia.getColumnDimension());
        this.workingVector = new Matrix(1, Incidencia.getColumnDimension());
        this.alphaTime = new Matrix(aTimes, 1);
        this.sensibilizedTime = new Matrix(1, Incidencia.getColumnDimension());
        this.firedSequence = new ArrayList<String>();
        this.countT = new Matrix(1, cantTransiciones);
        this.tInvariantSum = new ArrayList<>();
        this.tInvariantsAux = new ArrayList<>();
        for (int j = 0; j < cantTransiciones; j++)
        {
            countT.set(0, j, 0.0);
        }
    }


    /*
     * ********************
     * Public Methods *
     ********************
     */

    /*
     * This method calculates the fundamental ecuation of the petrinet: mi+1= mi+W*s
     * where mi is the current marking, W is the Incidencia matrix and s is the firing vector.
     * 
    * @param v: firing vector
    * @return fundamental equation
    */
    public Matrix fundamentalEquation(Matrix v)
    {
        return (marcaActual.transpose().plus(Incidencia.times(v.transpose()))).transpose();
        // (mi + w * s) transpose
    }

    public boolean fundamentalEquationTest(Matrix firingVector)
    {
        matriz = fundamentalEquation(firingVector);
        for (int i = 0; i < this.matriz.getColumnDimension(); i++)
            if (this.matriz.get(0, i) < 0) {
                return false;
            }
        return true;
    }

    /*
     * Idea: compare the current marking to the marking requested for each transition. 
     * Current marking: vector with the individual marking of all the places
     * Incidencia matrix: columns = transitions | rows = places
     * Then, if a transition of the current marking has fewer tokens than those requested by the transition, it cannot be fired.
     * 
     * @param 
     * @return 
     */
    void enableTransitions()
    {
        Long time = System.currentTimeMillis();// tiempo actual
        for (int i = 0; i < inversaIncidencia.getColumnDimension(); i++)
        {
            boolean enabledTransition = true;
            for (int j = 0; j < inversaIncidencia.getRowDimension(); j++)
            {
                if (inversaIncidencia.get(j, i) > marcaActual.get(0, j))
                {
                    enabledTransition = false;
                    break;
                }
            }
            if (enabledTransition)
            {
                vecTsensibilizadas.set(0, i, 1);
                sensibilizedTime.set(0, i, (double) time);
            } else
            {
                vecTsensibilizadas.set(0, i, 0);
            }

        }
        //System.out.println("Enabled transitions: " + getEnabledTransitionsInfo());
    }

    /*
     * returns a string with the enabled transitions info.
     * 
     * @return enabled transitions info
     */
    public String getEnabledTransitionsInfo()
    {
        String enabled = "";
        for (int i = 0; i < cantTransiciones; i++) {
            if (vecTsensibilizadas.get(0, i) == 1)
            {
                enabled += ("T" + i + "  ");
            }
        }
        return enabled;
    }

    public String getAllTransitionsPrint() {
        return allTransitionsPrint;
    }

    /*
     * 
     * This method fires a transition if it is enabled.
     * - change current marking.
     * - update current working vector.
     * - update sensibilized transitions.
     * - adds to the fired secuence the fired transition.
     * 
     * @param v: firing vector
     * @return
     */

    void fire(Matrix v) // esta es la que hace el disparo literal, actualizando la rdp
    {
        // Construir una cadena con los valores del vector de disparo
    StringBuilder firingVectorStr = new StringBuilder("Firing vector: [");
    for (int i = 0; i < v.getRowDimension(); i++) {
        for (int j = 0; j < v.getColumnDimension(); j++) {
            firingVectorStr.append(v.get(i, j));
            if (j < v.getColumnDimension() - 1) {
                firingVectorStr.append(", ");
            }
        }
        if (i < v.getRowDimension() - 1) {
            firingVectorStr.append("; ");
        }
    }
    firingVectorStr.append("]");
    System.out.println(firingVectorStr.toString()); // Imprimir el vector de disparo


        setmarcaActual(fundamentalEquation(v));
        setWorkingVector(v, 0);
        testPInvariants();
        enableTransitions();
        firedSequence.add("T" + getIndex(v) + ""); // tiene todas las secuencia de transiciones disparadas
        System.out.println("Firing: T" + getIndex(v));
        for (int i = 0; i < v.getRowDimension(); i++)
        {
            for (int j = 0; j < v.getColumnDimension(); j++)
            {
                if (v.get(i, j) != 0.0)
                {
                    countT.set(i, j, (countT.get(i, j) + v.get(i, j)));
                }
            }
        }
        System.out.println(transitionsCounterInfo());
        System.out.println("Current marking:\n" + getMarkingInfo());
        String lastTransition = getIndex(v) + "";
        allTransitionsPrint += "T" + lastTransition;
        followUp(lastTransition);

    }

    public String getMarkingInfo()
    {
        String marking = "P0 P1 P2 P3 P4 P5 P6 P7 P8 P9 P10 P11 P12 P13 P14\n";
        for (int i = 0; i < marcaActual.getColumnDimension(); i++)
        {
            if (i < 10) //Estetica de impresion
            {
                marking += ((int) marcaActual.get(0, i) + "  ");
            } else
            {
                marking += ((int) marcaActual.get(0, i) + "   ");
            }
        }
        return (marking + "\n");
    }

    public int getCompletedInvariants() {
        return completedInvariants;
    }

    public int getValinvariantCounting(int i) {
        return countIT[i];
    }

    public String transitionsCounterInfo()
    {
        int totalCount = 0;
        String arg = "Transitions:\n";
        for (int i = 0; i < countT.getColumnDimension(); i++)
        {
            arg += ("                         🔹T" + i + ": " + (int) countT.get(0, i) + " times\n");
            totalCount += (int) countT.get(0, i);
        }
        arg += ("                                   🔹Total transitions: " + totalCount);
        return arg;
    }


    /*
     * Checks the 'state' of the transition that is going to be fired.
     * 0 - No one is working on it. STATE = NONE
     * 1 - Someone is working on it, but it is not the thread that is requesting it. STATE = OTHER
     * 2 - The thread that is requesting it is already working on it. STATE = SELF
     * 
     * @param v: firing vector
     * @return
     */

    public int workingState(Matrix v)
    {
        int index = getIndex(v);

        if (workingVector.get(0, index) == 0)
            return 0;
        else if (workingVector.get(0, index) != Thread.currentThread().getId())
            return 1;
        else
            return 2;
    }


    /*
     * Follows the transitions fired in order to check the T-invariants.
     * 
     * @param lastTransition: last transition fired
     * @return
     */
    public void followUp(String lastTransition)
    {

        if (!lastTransition.contains("11"))
        {
            rutaActual += lastTransition;
        } else
        {
            if (rutaActual.contains("01347811"))
            {
                countIT[0] += 1;
                completedInvariants++;
            } else if (rutaActual.contains("0134691011"))
            {
                countIT[1] += 1;
                completedInvariants++;
            } else if (rutaActual.contains("01257811"))
            {
                countIT[2] += 1;
                completedInvariants++;
            } else if (rutaActual.contains("0125691011"))
            {
                countIT[3] += 1;
                completedInvariants++;
            } else
            {

                for (int i = 0; i < cantIT; i++)
                {
                    System.out.println(i + " T-invariant appears " + getValinvariantCounting(i) + " times.");
                }
                System.out.println("Error in T-Invariant: " + rutaActual
        );
            }

            for (int i = 0; i < cantIT; i++)
            {
                System.out.println(i + " T-invariant appears " + getValinvariantCounting(i) + " times.");
            }

            rutaActual = "";
        }

    }

    public void tInvariantsInfo()
    {
        for (int i = 0; i < cantIT; i++)
        {
            System.out.println(i + " T-invariant appears " + getValinvariantCounting(i) + " times.");
        }
    }

    /*
     * This method checks the P-invariants using the current marking of the places.
     * 
     * @param
     * @return
     */
    public void testPInvariants()
    {
        boolean IP0, IP1, IP2, IP3, IP4, IP5;
        IP0 =(marcaActual.get(0, 0) + marcaActual.get(0, 2) 
            + marcaActual.get(0, 3) + marcaActual.get(0, 5) 
            + marcaActual.get(0, 8) + marcaActual.get(0, 9)
            + marcaActual.get(0, 11) + marcaActual.get(0, 12) 
            + marcaActual.get(0, 13) + marcaActual.get(0, 14)) == 5;
        IP1 = (marcaActual.get(0, 1) + marcaActual.get(0, 2)) == 1;
        IP2 = (marcaActual.get(0, 2) + marcaActual.get(0, 3) + marcaActual.get(0, 4)) == 5;
        IP3 = (marcaActual.get(0, 5) + marcaActual.get(0, 6)) == 1;
        IP4 = (marcaActual.get(0, 7) + marcaActual.get(0, 8)) == 1;
        IP5 = (marcaActual.get(0, 10) + marcaActual.get(0, 11)
            + marcaActual.get(0, 12) + marcaActual.get(0, 13)) == 1;

        if (!(IP0 && IP1 && IP2 && IP3 && IP4 && IP5))
        {
            System.out.println("Error in a p-invariant.");
            System.out.println(IP0 + "," + IP1 + "," + IP2 + "," + IP3 + "," + IP4 + "," + IP5);
        }
    }


    public Matrix getmarcaActual() {
        return marcaActual;
    }

    public void setmarcaActual(Matrix marcaActual) {
        this.marcaActual = marcaActual;
    }

    public Matrix getSensibilized() {
        return vecTsensibilizadas;
    }

    /*
     * Returns the index of the transition that is going to be fired.
     * 
     * @param v: firing vector
     * @return index of the transition
    */
    public int getIndex(Matrix v)
    {
        int index = 0;
        for (int i = 0; i < v.getColumnDimension(); i++)
        {
            if (v.get(0, i) == 1)
                break;
            else
                index++;
        }
        return index;
    }

    public Matrix getAlphaTimes() {
        return alphaTime;
    }

    public Matrix getSensibilizedTime() {
        return sensibilizedTime;
    }

    public void setWorkingVector(Matrix firingVector, double value)
    {
        this.workingVector.set(0, getIndex(firingVector), value);
    }

    public Matrix getIncidenceMatrix() {
        return this.Incidencia;
    }
}