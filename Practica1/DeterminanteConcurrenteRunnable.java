public class DeterminanteConcurrenteRunnable implements Runnable {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    int num1, num2, num3, partial;

    public DeterminanteConcurrenteRunnable(int num1, int num2, int num3) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
    }

    public static int determinanteMatriz3x3(int matriz[][], int n_prueba) {
        int result = 0;
        DeterminanteConcurrenteRunnable task1 = new DeterminanteConcurrenteRunnable(matriz[0][0], matriz[1][1], matriz[2][2]);
        DeterminanteConcurrenteRunnable task2 = new DeterminanteConcurrenteRunnable(matriz[1][0], matriz[2][1], matriz[0][2]);
        DeterminanteConcurrenteRunnable task3 = new DeterminanteConcurrenteRunnable(matriz[2][0], matriz[0][1], matriz[1][2]);
        DeterminanteConcurrenteRunnable task4 = new DeterminanteConcurrenteRunnable(matriz[2][0], matriz[1][1], matriz[0][2]);
        DeterminanteConcurrenteRunnable task5 = new DeterminanteConcurrenteRunnable(matriz[1][0], matriz[0][1], matriz[2][2]);
        DeterminanteConcurrenteRunnable task6 = new DeterminanteConcurrenteRunnable(matriz[0][0], matriz[2][1], matriz[1][2]);

        Thread thr1 = new Thread(task1);
        Thread thr2 = new Thread(task2);
        Thread thr3 = new Thread(task3);
        Thread thr4 = new Thread(task4);
        Thread thr5 = new Thread(task5);
        Thread thr6 = new Thread(task6);

        thr1.start();
        thr2.start();
        thr3.start();
        thr4.start();
        thr5.start();
        thr6.start();

        try {
            thr1.join();
            thr2.join();
            thr3.join();
            thr4.join();
            thr5.join();
            thr6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = task1.partial + task2.partial + task3.partial - task4.partial - task5.partial - task6.partial;

        return result;
    }

    @Override
    public void run() {
        this.partial = this.num1 * this.num2 * this.num3;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " + (endTime - startTime) + "ns, result: " + determinante);
    }
}
