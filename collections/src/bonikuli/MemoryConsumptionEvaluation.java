package bonikuli;

import java.util.ArrayList;

public class MemoryConsumptionEvaluation {
    
    private ArrayList<String> arrayList;
    private int[] array;
    
    public static void main(String[] args) {
        try {
            MemoryConsumptionEvaluation test = new MemoryConsumptionEvaluation();
            test.evaluateForArrayList();
            test.evaluateForArray();
        } catch (InterruptedException e) {
            System.out.println("Execution were interrupted");
        }
    }
    
    private void evaluateForArrayList() throws InterruptedException {
        arrayList = new ArrayList<>(1000);
        System.out.println("Created array list of 1000 capacity. Approximiate allocated size is <Object Refence Size> * 1000. I saw 8056 bytes for JVM8 at 64bit Windows OS. And what do you see?");
        //Thread.sleep(20000);
        arrayList.ensureCapacity(10000);
        System.out.println("Allocated more memory for array list to meet 10000 capacity. Approximiate allocated size is <Object Refence Size> * 10000. I saw 80056 bytes for JVM8 at 64bit Windows OS. And what do you see?");
        //Thread.sleep(20000);
        arrayList = null;
        System.out.println("Array list is free for garbage collection");
    }
    
    private void evaluateForArray() throws InterruptedException {
        array = new int[1000];
        System.out.println("Created array of 1000 size. Approximiate allocated size is <int Size> * 1000. I saw 4024 bytes for JVM8 at 64bit Windows OS. And what do you see?");
        //Thread.sleep(20000);
        array = new int[10000];
        System.out.println("Created array of 10000 size. Approximiate allocated size is <int Size> * 1000. I saw 40024 bytes for JVM8 at 64bit Windows OS. And what do you see?");
        //Thread.sleep(20000);
        array = null;
        System.out.println("Array is free for garbage collection");
    }
}
