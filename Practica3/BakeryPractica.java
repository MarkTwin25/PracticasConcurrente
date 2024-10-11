import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BakeryPractica {
    private static final int NUM_THREADS = 4;
    private static final int NUM_TASKS = 400;

    public static void main(String[] args) throws InterruptedException {
        BakeryTeoria bakeryLock = new BakeryTeoria(NUM_THREADS); // Inicializa con el número de hilos
        CounterNaive counter = new CounterNaive();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        int[] threadCounts = new int[NUM_THREADS];

        // Calcula cuántas tareas asignar a cada hilo
        int tasksPerThread = NUM_TASKS / NUM_THREADS;
        int remainingTasks = NUM_TASKS % NUM_THREADS; // Las tareas restantes se dividen entre los primeros hilos

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            int tasksForThisThread = tasksPerThread + (threadId < remainingTasks ? 1 : 0); // Asignar tareas restantes

            executor.submit(() -> {
                Thread.set(threadId);
                for (int j = 0; j < tasksForThisThread; j++) {
                    bakeryLock.lock();
                    try {
                        int value = counter.increment();
                        threadCounts[threadId]++;
                        System.out.println("Hilo " + threadId + " incrementó el contador a " + value);
                    } finally {
                        bakeryLock.unlock();
                    }
                }
            });
        }

        executor.shutdown();
        
        // Esperar a que todos los hilos terminen
        while (!executor.isTerminated()) {
        }

        System.out.println("Valor final del contador: " + counter.getValue());
        System.out.println("Distribución de tareas por hilo:");
        for (int i = 0; i < NUM_THREADS; i++) {
            System.out.println("Hilo " + i + ": " + threadCounts[i] + " tareas");
        }
    }
}
