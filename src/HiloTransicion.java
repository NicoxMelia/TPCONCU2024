// Hilo que ejecuta una transición (CADA TRANSICION DEBE TENER UN HILO INDEPENDIENTE, EL CUAL SE TRATA
//DE LANZAR CONTINUAMENTE {SEGUN GPT})
public class HiloTransicion implements Runnable {
    private Monitor monitor;
    private int transition;

    public HiloTransicion(Monitor monitor, int transition) {
        this.monitor = monitor;
        this.transition = transition;
    }

    @Override
    public void run() {
        while (true) {
            if (monitor.fireTransition(transition)) {
                System.out.println("Transición " + transition + " disparada.");
            }
            try {
                Thread.sleep(1000); // Simulación de tiempo entre disparos: 1 seg
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
