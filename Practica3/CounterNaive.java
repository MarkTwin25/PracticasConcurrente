public class CounterNaive {
    private int count = 0;

    // Método sincronizado para incrementar el contador
    public synchronized int increment() {
        return ++count;
    }

    public synchronized int getValue() {
        return count;
    }
}
