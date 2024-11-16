package Practica6;

import java.util.concurrent.atomic.AtomicReference;

public class CASConsensus<T> {
    private AtomicReference<T> decision = new AtomicReference<>(null);

    public T decide(T value, int threadId) {
        if (decision.compareAndSet(null, value)) {
            System.out.println("Thread " + threadId + " estableció el consenso con valor: " + value);
        } else {
            System.out.println("Thread " + threadId + " observó consenso existente: " + decision.get());
        }
        return decision.get();
    }

    public boolean hasDecision() {
        return decision.get() != null;
    }
}