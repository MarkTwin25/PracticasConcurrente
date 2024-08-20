public class Procesadores {
    public static void main(String[] args) {
        
        int numProcesadores = Runtime.getRuntime().availableProcessors();
        System.out.println("Esta computadora tiene: " + numProcesadores + " procesadores.");
    }
}
