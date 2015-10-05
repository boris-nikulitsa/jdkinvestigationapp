package jdkinvestigationapp.algorithms;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class AnalyzeJDK6MergeAlg {
    
    private static int counter;
    private final static int SIZE = 7;
    private static final int INSERTIONSORT_THRESHOLD = 8;
    
    public static void main(String[] args) throws Exception {
        String reportName = AnalyzeJDK6MergeAlg.class.getSimpleName() + "_THRESHOLD" + INSERTIONSORT_THRESHOLD + "_SIZE" + SIZE + ".txt";
        try (PrintWriter out = new PrintWriter(new FileOutputStream(reportName))) {
            long total_counter = 0;
            PermutationGenerator gen = new PermutationGenerator(SIZE);
            Integer[] permutation = gen.next();
            while (permutation != null) {
                out.print(Arrays.toString(permutation));
                counter = 0;
                sort(permutation);
                out.println(" : " + counter);
                total_counter += counter;
                permutation = gen.next();
            }
            System.out.println("Total: " + total_counter);
            out.println("Total: " + total_counter);
        }
    }
    
// ------------- There's modified jdk1.6.0_101 source code to calculate number of copmarisons ----------------
    
    public static void sort(Object[] a) {
        Object[] aux = (Object[])a.clone();
        mergeSort(aux, a, 0, a.length, 0);
    }
    
    private static void mergeSort(Object[] src,
				  Object[] dest,
				  int low,
				  int high,
				  int off) {
	int length = high - low;

	// Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD) {
            for (int i=low; i<high; i++)
                for (int j=i; j>low &&
			 compareTo((Comparable) dest[j-1],dest[j])>0; j--)
                    swap(dest, j, j-1);
            return;
        }

        // Recursively sort halves of dest into src
        int destLow  = low;
        int destHigh = high;
        low  += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off);
        mergeSort(dest, src, mid, high, -off);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (compareTo((Comparable)src[mid-1],src[mid]) <= 0) {
            System.arraycopy(src, low, dest, destLow, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && compareTo((Comparable)src[p],src[q])<=0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }

    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(Object[] x, int a, int b) {
	Object t = x[a];
	x[a] = x[b];
	x[b] = t;
    }
    
    private static int compareTo(Comparable first, Object second) {
        counter++;
        return first.compareTo(second);
    }
    
}
