class BakeryTeoria {
    private volatile boolean[] flag;
    private volatile int[] label;
    private final int THREADS;

    public BakeryTeoria(int threads) {
        this.THREADS = threads;
        flag = new boolean[THREADS];
        label = new int[THREADS];
        for (int i = 0; i < THREADS; i++) {
            flag[i] = false;
            label[i] = 0;
        }
    }

    public void lock() {
        int i = Thread.get();
        flag[i] = true;
        label[i] = max(label) + 1;

        for (int k = 0; k < THREADS; k++) {
            while (k != i && flag[k] && 
                   (label[k] < label[i] || (label[k] == label[i] && k < i))) {
                // Espera activa
            }
        }
    }

    public void unlock() {
        flag[Thread.get()] = false;
    }

    private int max(int[] arr) {
        int max = 0; // Asignar el valor mínimo posible
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
