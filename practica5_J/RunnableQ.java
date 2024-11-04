package unam.fc.concurrent.practica5;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunnableQ<T> implements Runnable {
    private int item;
    ConcurrentLinkedQueue<Integer> queue;
    SimpleSnapshot<String> snapshotI;
    SimpleSnapshot<String> snapshotR;
    private int num;

    public RunnableQ(int item, ConcurrentLinkedQueue<Integer> queue,
                     int num, SimpleSnapshot<String> snapshotI, SimpleSnapshot<String> snapshotR) {
        this.item = item;
        this.queue = queue;
        this.snapshotI = snapshotI;
        this.snapshotR = snapshotR;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            if (this.num == 0) { // Ejecutar enq
                String string = String.format("enq( %s )", this.item);
                snapshotI.update(string); // Registrar la invocación

                Boolean res = this.queue.add(this.item);

                Object[] result = snapshotI.scan(); // Realizar un collect
                String scan = Arrays.stream(result)
                        .map(s -> s != null ? s.toString() : "null")
                        .collect(Collectors.joining(" + "));
                snapshotR.update(scan + String.format(" |  %s || ", res)); // Registrar la respuesta

            } else if (this.num == 1) { // Ejecutar deq
                snapshotI.update("deq()");
                Integer res = this.queue.poll();

                Object[] result = snapshotI.scan(); // Realizar un collect
                String scan = Arrays.stream(result)
                        .map(s -> s != null ? s.toString() : "null")
                        .collect(Collectors.joining(" + "));
                snapshotR.update(scan + String.format(" |  %s || ", res));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
