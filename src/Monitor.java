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
