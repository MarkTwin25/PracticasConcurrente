package Practica6;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecConsenRounds {
    public static volatile int[] winners;
    // Matriz para rastrear las propuestas de cada hilo en cada ronda
    public static volatile boolean[][] hasProposed;

    public static void initializeRounds(int numRounds, int numThreads) {
        winners = new int[numRounds];
        Arrays.fill(winners, -1);
        // Inicializa la matriz de propuestas
        hasProposed = new boolean[numRounds][numThreads];
        for (boolean[] round : hasProposed) {
            Arrays.fill(round, false);
        }
    }

    // Método que implementa la ayuda wait-free para otros hilos
    public static void helpOtherThreads(int currentRound, CASConsensus<Integer> cas, int numThreads, int myId) {
        for (int threadId = 0; threadId < numThreads; threadId++) {
            // Ayuda solo a hilos que no han propuesto y que no son el hilo actual
            if (!hasProposed[currentRound][threadId] && threadId != myId) {
                int result = cas.decide(threadId, myId);
                hasProposed[currentRound][threadId] = true;
                synchronized (ExecConsenRounds.class) {
                    if (winners[currentRound] == -1) {
                        winners[currentRound] = result;
                    }
                }
            }
        }
    }

    public static void task(CountDownLatch latch, CASConsensus<Integer> cas, int numThreads, int round) {
        Thread thread = Thread.currentThread();
        int id = (int) (thread.getId() % numThreads);

        // Marca que este hilo ha hecho su propuesta
        hasProposed[round][id] = true;
        int winner = cas.decide(id, id);

        synchronized (ExecConsenRounds.class) {
            if (winners[round] == -1) {
                winners[round] = winner;
            }
        }

        helpOtherThreads(round, cas, numThreads, id);

        // Asegura que todos los hilos hayan participado antes de terminar
        boolean allThreadsParticipated;
        do {
            allThreadsParticipated = true;
            for (int i = 0; i < numThreads; i++) {
                if (!hasProposed[round][i]) {
                    allThreadsParticipated = false;
                    helpOtherThreads(round, cas, numThreads, id);
                    break;
                }
            }
        } while (!allThreadsParticipated);

        System.out.println("Round: " + round + "\nThread: " + id + " says WIN: " + winner);
        latch.countDown();
    }

    public static void main(String[] args) {
        final int numThreads = 4;
        final int numRounds = 10;

        initializeRounds(numRounds, numThreads);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int round = 0; round < numRounds; round++) {
            final CASConsensus<Integer> cas = new CASConsensus<>();
            final CountDownLatch latch = new CountDownLatch(numThreads);

            final int currentRound = round;
            for (int i = 0; i < numThreads; i++) {
                executor.submit(() -> task(latch, cas, numThreads, currentRound));
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        System.out.println("\nWinners: " + Arrays.toString(winners));
    }
}
