public class DeterminanteComparacion {
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    static int NUM_RUNS = 10000;

    public static void main(String[] args) {
        long totalSecuencialTime = 0;
        long totalDosHilosTime = 0;
        long totalSeisHilosTime = 0;

        int secuencialResult = 0, dosHilosResult = 0, seisHilosResult = 0;

        for (int i = 0; i < NUM_RUNS; i++) {
            // Versión secuencial
            long startTime = System.nanoTime();
            secuencialResult = determinanteMatriz3x3Secuencial(matriz_prueba);
            long endTime = System.nanoTime();
            totalSecuencialTime += (endTime - startTime);

            // Versión con dos hilos
            startTime = System.nanoTime();
            dosHilosResult = DeterminanteConcurrenteDosHilos.determinanteMatriz3x3(matriz_prueba, n_prueba);
            endTime = System.nanoTime();
            totalDosHilosTime += (endTime - startTime);

            // Versión con seis hilos
            startTime = System.nanoTime();
            seisHilosResult = DeterminanteConcurrenteRunnable.determinanteMatriz3x3(matriz_prueba, n_prueba);
            endTime = System.nanoTime();
            totalSeisHilosTime += (endTime - startTime);
        }

        double avgSecuencialTime = totalSecuencialTime / (double)NUM_RUNS;
        double avgDosHilosTime = totalDosHilosTime / (double)NUM_RUNS;
        double avgSeisHilosTime = totalSeisHilosTime / (double)NUM_RUNS;

        System.out.println("Resultados:");
        System.out.println("Secuencial: " + secuencialResult);
        System.out.println("Dos hilos: " + dosHilosResult);
        System.out.println("Seis hilos: " + seisHilosResult);

        System.out.println("\nTiempos promedio:");
        System.out.println("Secuencial: " + avgSecuencialTime + " ns");
        System.out.println("Dos hilos: " + avgDosHilosTime + " ns");
        System.out.println("Seis hilos: " + avgSeisHilosTime + " ns");

        System.out.println("\nRelaciones:");
        System.out.println("Secuencial vs Dos hilos: " + avgSecuencialTime / avgDosHilosTime);
        System.out.println("Secuencial vs Seis hilos: " + avgSecuencialTime / avgSeisHilosTime);
        System.out.println("Dos hilos vs Seis hilos: " + avgDosHilosTime / avgSeisHilosTime);
    }

    public static int determinanteMatriz3x3Secuencial(int matriz[][]) {
        return matriz[0][0] * matriz[1][1] * matriz[2][2] +
               matriz[1][0] * matriz[2][1] * matriz[0][2] +
               matriz[2][0] * matriz[0][1] * matriz[1][2] -
               matriz[2][0] * matriz[1][1] * matriz[0][2] -
               matriz[1][0] * matriz[0][1] * matriz[2][2] -
               matriz[0][0] * matriz[2][1] * matriz[1][2];
    }
}