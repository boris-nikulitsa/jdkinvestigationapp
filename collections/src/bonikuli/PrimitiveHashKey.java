package bonikuli; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class PrimitiveHashKey {
    
    private final HashMap<int[], Integer> array_map = new HashMap<>();
    private final HashMap int_map = new HashMap();
    
    public static void main(String[] args) {
        PrimitiveHashKey test = new PrimitiveHashKey();
        doForArray(test);
        doForInt(test);
    }
    
    private static void doForInt(PrimitiveHashKey test) {
        Random rnd = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=0; i<1000; ++i) {
            int key = rnd.nextInt();
            test.int_map.put(key, i);
            list.add(key);
        }
        for (int i=0; i<1000; ++i) {
            int original_key = list.get(i);
            if (!Integer.valueOf(i).equals(test.int_map.get(original_key))) {
                System.out.println("Failed for iteration=" + i + "; original key=" + original_key);
            }
        }
    }
    
    private static void doForArray(PrimitiveHashKey test) {
        Random rnd = new Random();
        ArrayList<int[]> list = new ArrayList<>();
        for (int i=0; i<1000; ++i) {
            int[] key = generateArray(rnd);
            test.array_map.put(key, i);
            list.add(key);
        }
        for (int i=0; i<1000; ++i) {
            int[] original_key = list.get(i);
            if (!Integer.valueOf(i).equals(test.array_map.get(original_key))) {
                System.out.println("Failed for iteration=" + i + "; original key=" + Arrays.toString(original_key));
            }
        }
        for (int i=0; i<1000; ++i) {
            int[] original_key = list.get(i);
            int[] key_copy = Arrays.copyOf(original_key, original_key.length);
            if (!Integer.valueOf(i).equals(test.array_map.get(key_copy))) {
                System.out.println("Failed for iteration=" + i + "; original key=" + Arrays.toString(original_key) + "; key copy=" + Arrays.toString(key_copy));
            }
        }
    }
    
    private static int[] generateArray(Random rnd) {
        int size = rnd.nextInt(35) + 5;
        int[] value = new int[size];
        for (int i=0; i<size; ++i) {
            value[i] = rnd.nextInt();
        }
        return value;
    }
}
