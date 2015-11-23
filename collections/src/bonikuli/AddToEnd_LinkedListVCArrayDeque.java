package bonikuli;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/*
ArrayDeque is faster based on java documentation. But it's interesting that 
ArrayDeque reallocation is also quite fast operation because array copy is native operation.
Most slow ArrayDeque was during iteration 4194303. That is during one of reallocation : 4194304 = 2 ^ 23.

I got below results:
#1
iteration 4194303 : ArrayDeque slower than LinkedList on 139903380 nanoseconds
iteration 7170686 : ArrayDeque faster than LinkedList on 208956485 nanoseconds
in total ArrayDeque faster than LinkedList on 9906751 nanoseconds

#2
iteration 4194303 : ArrayDeque slower than LinkedList on 170972758 nanoseconds
iteration 5623026 : ArrayDeque faster than LinkedList on 165072449 nanoseconds
in total ArrayDeque slower than LinkedList on 57889223 nanoseconds

#3
iteration 4194303 : ArrayDeque slower than LinkedList on 155170051 nanoseconds
iteration 2842590 : ArrayDeque faster than LinkedLIst on 893055928 nanoseconds
in total ArrayDeque faster than LinkedList on 997986738 nanoseconds
*/
public class AddToEnd_LinkedListVCArrayDeque {
    
    public static void main(String[] args) {
        evaluate(new ArrayDeque<>(), new LinkedList<>());
    }
    
    private static void evaluate(Collection<Integer> first, Collection<Integer> second) {
        Random rnd = new Random();
        BigInteger winnerResult = BigInteger.valueOf(0);
        long min = 0, max = 0;
        int minIndex = 0, maxIndex = 0;
        int reallocationHappens = 7;
        for (int i=0; i<10000000; ++i) {
            if (reallocationHappens < i) {
                reallocationHappens  = ((reallocationHappens + 1) << 1 ) - 1;
            }
            Integer value = rnd.nextInt();
            long t1 = System.nanoTime();
            first.add(value);
            long t2 = System.nanoTime();
            second.add(value);
            long t3 = System.nanoTime();
            if (i < 20000) {
                // warm up JVM
            } else {
                long shift = t3 - t2 + t1 - t2;
                winnerResult = winnerResult.add(BigInteger.valueOf(shift));
                if (reallocationHappens == i) { // (shift < -1000 || shift > 1000) {
                    System.out.println(i + " : " + shift);
                }
                if (shift < min) {
                    min = shift;
                    minIndex = i;
                }
                if (shift > max) {
                    max = shift;
                    maxIndex = i;
                }
            }
        }
        System.out.println("Sum : " + winnerResult.toString());
        System.out.println(minIndex + " : " + min);
        System.out.println(maxIndex + " : " + max);
    }
}
