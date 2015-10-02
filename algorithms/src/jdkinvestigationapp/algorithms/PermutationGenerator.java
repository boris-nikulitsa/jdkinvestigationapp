package jdkinvestigationapp.algorithms;

import java.util.Arrays;

public class PermutationGenerator {
    
    private final int size;
    private Integer[] source;
    private boolean[] used;
    
    public PermutationGenerator(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("To small size " + size);
        }
        this.size = size;
        source = new Integer[size];
        used = new boolean[size];
    }
    
    /**
     * @return next permutation if it's exist; null if all possible permutations are generated
     * @throws IllegalStateException if cannot generate next permutation
     */
    public Integer[] next() {
        if (source == null) {
            throw new IllegalStateException("All permutations already have been generated");
        }
        if (source[0] == null) {
            // initialize
            for (int i=0; i<size; ++i) {
                source[i] = i;
                used[i] = true;
            }
            return source;
        }
        
        int index = size - 1;
        while (index >= 0 && index < size) {
            Integer value = source[index];
            if (value != null) {
                used[value] = false;
                source[index] = null;
            }
            for (int i=(value == null ? 0 : value+1); source[index] == null && i<size; ++i) {
                if (!used[i]) {
                    source[index] = i;
                    used[i] = true;
                }
            }
            if (source[index] == null) {
                // need to change previous element before
                --index;
            } else {
                // can change next element now
                ++index;
            }
        }
        if (index < 0) {
            source = null;
        }
        return source == null ? null : Arrays.copyOf(source, source.length);
    }
    
}
