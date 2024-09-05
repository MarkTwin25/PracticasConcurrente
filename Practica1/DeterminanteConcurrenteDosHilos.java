public class DeterminanteConcurrenteDosHilos implements Runnable {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    int[][] matriz;
    int[] partials;
    int startIndex;

    public DeterminanteConcurrenteDosHilos(int[][] matriz, int[] partials, int startIndex) {
        this.matriz = matriz;
        this.partials = partials;
        this.startIndex = startIndex;
    }

    public static int determinanteMatriz3x3(int matriz[][], int n_prueba) {
        int[] partials = new int[6];
        
        DeterminanteConcurrenteDosHilos task1 = new DeterminanteConcurrenteDosHilos(matriz, partials, 0);
        DeterminanteConcurrenteDosHilos task2 = new DeterminanteConcurrenteDosHilos(matriz, partials, 3);

        Thread thr1 = new Thread(task1);
        Thread thr2 = new Thread(task2);

        thr1.start();
        thr2.start();

        try {
            thr1.join();
            thr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return partials[0] + partials[1] + partials[2] - partials[3] - partials[4] - partials[5];
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            int index = startIndex + i;
            if (index < 3) {
                partials[index] = matriz[index % 3][0] * matriz[(index + 1) % 3][1] * matriz[(index + 2) % 3][2];
            } else {
                partials[index] = matriz[index % 3][0] * matriz[(index - 1) % 3][1] * matriz[(index - 2) % 3][2];
            }
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " + (endTime - startTime) + "ns, result: " + determinante);
    }
}