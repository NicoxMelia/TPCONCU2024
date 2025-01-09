public class main {
    public static void main(String[] args) {
        PetriNet petriNet = new PetriNet();
        
        // Crear política: balanceada o priorizada
        Politica politica = new PoliticaBalanceada();
        // Politica politica = new PoliticaPriorizada();
        
        Monitor monitor = new Monitor(petriNet, politica);
        
        //Creacion de los 12 hilos (uno por cada transicion)
        for (int i = 0; i <= 11; i++) {
            new Thread(new HiloTransicion(monitor, i)).start();
        }
    }
}