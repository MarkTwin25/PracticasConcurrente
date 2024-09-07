import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ColaConcurrenteSynchronized {
    private Nodo head;
    private Nodo tail;

    public ColaConcurrenteSynchronized() {
        this.head = new Nodo("hnull");
        this.tail = new Nodo("tnull");
        this.head.next = this.tail;
    }

    public synchronized Boolean enq(String x) {
        Nodo newnode = new Nodo(x);
        if (this.head.next == this.tail) {
            newnode.next = this.tail;
            this.head.next = newnode;
        } else {
            Nodo last = this.tail.next;
            newnode.next = tail;
            last.next = newnode;
        }
        tail.next = newnode;
        return true;
    }

    public synchronized String deq() {
        if (this.head.next == this.tail) {
            return "empty";
        }
        Nodo first = this.head.next;
        this.head.next = first.next;
        return first.item;
    }

    public synchronized void print() {
        System.out.println("Print: ");
        Nodo pred = this.head;
        Nodo curr = pred.next;
        while (curr.item != "tnull") {
            pred = curr;
            curr = curr.next;
            System.out.println(pred.item);
        }
    }

    public static void main(String[] args) {
        ColaConcurrenteSynchronized queue = new ColaConcurrenteSynchronized();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            // Submitting tasks to executor
            Future<Boolean> enqFuture1 = executor.submit(() -> queue.enq("a"));
            Future<Boolean> enqFuture2 = executor.submit(() -> queue.enq("b"));
            Future<String> deqFuture1 = executor.submit(() -> queue.deq());
            Future<Boolean> enqFuture3 = executor.submit(() -> queue.enq("c"));
            Future<Boolean> enqFuture4 = executor.submit(() -> queue.enq("d"));
            Future<String> deqFuture2 = executor.submit(() -> queue.deq());
            Future<String> deqFuture3 = executor.submit(() -> queue.deq());

            // Revisar los resultados de las operaciones
            System.out.println("Enqueue 'a' result: " + enqFuture1.get());
            System.out.println("Enqueue 'b' result: " + enqFuture2.get());
            System.out.println("Dequeue result: " + deqFuture1.get());
            System.out.println("Enqueue 'c' result: " + enqFuture3.get());
            System.out.println("Enqueue 'd' result: " + enqFuture4.get());
            System.out.println("Dequeue result: " + deqFuture2.get());
            System.out.println("Dequeue result: " + deqFuture3.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
        }
        executor.shutdown();
        try {
            Thread.sleep(2000); // Espera para que todas las tareas terminen
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        // Imprimir el estado final de la cola
        queue.print();
    }
}

class Nodo {
    public String item;
    public Nodo next;
    public Nodo(String item) {
        this.item = item;
    }
}