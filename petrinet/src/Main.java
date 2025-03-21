public class Main {

    /*
     * VARIABLES
     */

    private static Integer[][] INCIDENCE_MATRIX = {
        // T0  T1  T2  T3  T4  T5  T6  T7  T8  T9  T10 T11
        { -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1 }, // P0
        { -1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 }, // P1
        {  1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 }, // P2
        {  0,  1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0 }, // P3
        { -1,  0,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0 }, // P4
        {  0,  0,  1,  0,  0, -1,  0,  0,  0,  0,  0,  0 }, // P5
        {  0,  0, -1,  0,  0,  1,  0,  0,  0,  0,  0,  0 }, // P6
        {  0,  0,  0, -1,  1,  0,  0,  0,  0,  0,  0,  0 }, // P7
        {  0,  0,  0,  1, -1,  0,  0,  0,  0,  0,  0,  0 }, // P8
        {  0,  0,  0,  0,  1,  1, -1, -1,  0,  0,  0,  0 }, // P9
        {  0,  0,  0,  0,  0,  0, -1, -1,  1,  0,  1,  0 }, // P10
        {  0,  0,  0,  0,  0,  0,  1,  0,  0, -1,  0,  0 }, // P11
        {  0,  0,  0,  0,  0,  0,  0,  1, -1,  0,  0,  0 }, // P12
        {  0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0 }, // P13
        {  0,  0,  0,  0,  0,  0,  0,  0,  1,  0,  1, -1 }  // P14 
    };
    private static Integer[] MAIN_PLACES = {
        1, // P0
        0, // P1
        1, // P2
        1, // P3
        0, // P4
        1, // P5
        0, // P6
        0, // P7
        1, // P8
        1, // P9
        0, // P10
        1, // P11
        1, // P12
        1, // P13
        1  // P14
    };
    private static Integer[] INITIAL_MARKING = {
        3, // P0
        1, // P1
        0, // P2
        0, // P3
        5, // P4
        0, // P5
        1, // P6
        1, // P7
        0, // P8
        0, // P9
        1, // P10
        0, // P11
        0, // P12
        0, // P13
        0  // P14
    };
    private static Integer[][] PLACES_SEGMENTS_MATRIX = {
        // P0  P1  P2  P3  P4  P5  P6  P7  P8  P9  P10 P11 P12 P13 P14
        {  1,  1,  1,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 }, // S0
        {  0,  0,  0,  1,  0,  1,  1,  0,  0,  1,  0,  0,  0,  0,  0 }, // S1
        {  0,  0,  0,  1,  0,  0,  0,  1,  1,  1,  0,  0,  0,  0,  0 }, // S2
        {  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  1,  0,  1,  1 }, // S3
        {  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  0,  1,  0,  1 }, // S4
        {  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1 }  // S5
    };
    private static Integer[][] TRANSITIONS_SEGMENTS_MATRIX = {
        // T0  T1  T2  T3  T4  T5  T6  T7  T8  T9  T10 T11
        {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 }, // S0
        {  0,  0,  1,  0,  0,  1,  0,  0,  0,  0,  0,  0 }, // S1
        {  0,  0,  0,  1,  1,  0,  0,  0,  0,  0,  0,  0 }, // S2
        {  0,  0,  0,  0,  0,  0,  1,  0,  0,  1,  1,  0 }, // S3
        {  0,  0,  0,  0,  0,  0,  0,  1,  1,  0,  0,  0 }, // S4
        {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1 }, // S5
    };
    private static Integer[] SEGMENTS_STARTS = {
        0, // S0
        3, // S1
        3, // S2
        9, // S3
        9, // S4
        14 // S5
    };
    private static Integer[] SEGMENTS_ENDS = {
        3,  // S0
        9,  // S1
        9,  // S2
        14, // S3
        14, // S4
        0   // S5
    };
    private static Integer[] MIN_DELAY_TIMES = {
        500, // T0
        700, // T1
        500, // T2
        500, // T3
        700, // T4
        700, // T5
        500, // T6
        500, // T7
        700, // T8
        700, // T9
        700, // T10
        500, // T11
    };
    private static Integer[] MAX_DELAY_TIMES = {
        500, // T0
        900, // T1
        500, // T2
        500, // T3
        900, // T4
        900, // T5
        500, // T6
        500, // T7
        900, // T8
        900, // T9
        900, // T10
        500, // T11
    };
    private static Integer[] PROBABILITIES = {
        75, // P1
        30, // P2
    };
    
    /*
     * MAIN METHOD
     */
    
    public static void main(String args[]) {

        Monitor monitor = new Monitor(
                INCIDENCE_MATRIX,
                MAIN_PLACES,
                INITIAL_MARKING,
                PLACES_SEGMENTS_MATRIX,
                TRANSITIONS_SEGMENTS_MATRIX,
                SEGMENTS_STARTS,
                SEGMENTS_ENDS,
                MIN_DELAY_TIMES,
                MAX_DELAY_TIMES,
                PROBABILITIES);

        monitor.start();
    }
}
