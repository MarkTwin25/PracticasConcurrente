import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ColaConcurrente {
    private Nodo head;
    private Nodo tail;

    public ColaConcurrente() {
        this.head = new Nodo("hnull");
        this.tail = new Nodo("tnull");
        this.head.next = this.tail;
    }

    public Boolean enq(String x) {
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

    public String deq() {
        if (this.head.next == this.tail) {
            return "empty";
        }
        Nodo first = this.head.next;
        this.head.next = first.next;
        return first.item;
    }

    public void print() {
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
        ColaConcurrente queue = new ColaConcurrente();
        ExecutorService executor = Executors.newFixedThreadPool(4); // Se crea un pool de 4 hilos

            // Ejemplo tarea
            /*executor.execute(() -> queue.deq());
            executor.execute(() -> queue.enq("x"));
            executor.execute(() -> queue.enq("a"));
            executor.execute(() -> queue.deq());
            executor.execute(() -> queue.enq("b"));
            executor.execute(() -> queue.enq("c"));
            executor.execute(() -> queue.deq());
            executor.execute(() -> queue.deq());
            executor.execute(() -> queue.deq());
            executor.execute(() -> queue.deq());
            executor.execute(() -> queue.enq("x"));
            */

        // Se envían las tareas al executor
        executor.execute(() -> queue.enq("a"));
        executor.execute(() -> queue.enq("b"));
        executor.execute(() -> queue.deq());
        executor.execute(() -> queue.enq("c"));
        executor.execute(() -> queue.enq("d"));
        executor.execute(() -> queue.deq());
        executor.execute(() -> queue.deq());
        
 
        // Se cierra el executor
        executor.shutdown();

        try {
            Thread.sleep(2000); // Espera para que todas las tareas terminen
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        queue.print(); // Se imprime la cola
    }
}


class Nodo {
	public String item;
	public Nodo next;
	public Nodo(String item) {
		this.item = item;
	}
}
