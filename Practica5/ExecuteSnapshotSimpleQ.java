package Practica5;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecuteSnapshotSimpleQ {
    public static void main(String[] args) {
        int capacity = 4;
        String init = null;
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        SimpleSnapshot<String> snapshotI = new SimpleSnapshot<String>(capacity, init);
        SimpleSnapshot<String> snapshotR = new SimpleSnapshot<String>(capacity, init);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        Random rand = new Random();
        for(int i = 0; i < 10; i++) {
            int numRand = rand.nextInt(2);
            final int ntask = i;
            executor.execute(new RunnableSimpleQ(ntask, queue, numRand, snapshotI, snapshotR)); 
        }
        executor.shutdown();
        
        while(!executor.isTerminated()) {};
        
        StampedValue<String>[] copy = (StampedValue<String>[]) new StampedValue[capacity];
		copy = snapshotR.collect();

		System.out.println("Snapshot Final --- Values");
		for (int j = 0; j < capacity; j++) {
			System.out.println("\n Thread Owner: " + copy[j].owner +
					" Last Stamp: " + copy[j].stamp);
			System.out.println("The values are: ");
			for (int i = 0; i < copy[j].values.size(); i++) {

				System.out.println(copy[j].values.get(i));
			}

		}
    }
}