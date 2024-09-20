import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Peterson {
    private volatile boolean[] flag = new boolean[4]; // Para 4 hilos
    private volatile int victim;

    public Peterson() {
        flag[0] = false; flag[1] = false; flag[2] = false; flag[3] = false;
        victim = -1;
    }

    public void lock(int i) {
        flag[i] = true;
        victim = i;
        for (int k = 0; k < flag.length; k++) {
            if (k != i) {
                while (flag[k] && victim == i) {
                    // Espera activa
                }
            }
        }
    }

    public void unlock(int i) {
        flag[i] = false;
    }
}

class CounterNaive {
    private int count = 0;

    public int increment() {
        return this.count++;
    }

    public int getValue() {
        return this.count;
    }
}

class LockPeterson {

    private static Callable<Long> task(Peterson lock, CounterNaive counter, int threadId) {
        return () -> {
            try {
                lock.lock(threadId);
                counter.increment();
                return Thread.currentThread().getId();
            } finally {
                lock.unlock(threadId);
            }
        };
    }

    public static void main(String[] args) {
        Peterson lock = new Peterson();
        CounterNaive counter = new CounterNaive();
        ExecutorService executor = Executors.newFixedThreadPool(4); // Ahora con 4 hilos
        List<Future<Long>> futures = new ArrayList<>(); // Para guardar los IDs de los hilos

        for (int i = 0; i < 400; i++) {
            int threadId = i % 4; // Usamos los IDs de 0 a 3 para los 4 hilos
            futures.add(executor.submit(task(lock, counter, threadId))); // Cada hilo incrementa el contador
        }
        executor.shutdown();

        Map<Long, Integer> threadIncrements = new HashMap<>(); // Para contar los incrementos por hilo
        try {
            Thread.sleep(500);
            System.out.println(counter.getValue());
            for (Future<Long> future : futures) { // Obtenemos los IDs de los hilos
                try {
                    long threadId = future.get(); 
					// Incrementamos el contador de incrementos por hilo
                    threadIncrements.put(threadId, threadIncrements.getOrDefault(threadId, 0) + 1);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Incrementos por hilo:");
        for (Map.Entry<Long, Integer> entry : threadIncrements.entrySet()) {
            System.out.println("Hilo " + entry.getKey() + ": " + entry.getValue() + " incrementos");
        }
    }
}
