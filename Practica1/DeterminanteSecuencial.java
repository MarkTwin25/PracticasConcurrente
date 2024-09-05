public class DeterminanteSecuencial {
    static int determinante;
    static int n_prueba = 3;
    static int[][] matriz_prueba = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    
    
    public static int determinanteMatriz3x3(int[][] matriz) {
        return matriz[0][0] * matriz[1][1] * matriz[2][2] +
               matriz[1][0] * matriz[2][1] * matriz[0][2] +
               matriz[2][0] * matriz[0][1] * matriz[1][2] -
               matriz[2][0] * matriz[1][1] * matriz[0][2] -
               matriz[1][0] * matriz[0][1] * matriz[2][2] -
               matriz[0][0] * matriz[2][1] * matriz[1][2];
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba);
        long endTime = System.nanoTime();
        System.out.println("Sequential program took " +
                (endTime - startTime) + "ns, result: " + determinante);
    }
}