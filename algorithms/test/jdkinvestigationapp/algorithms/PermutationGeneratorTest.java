package jdkinvestigationapp.algorithms;

import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PermutationGeneratorTest {
    
    @Test
    public void testFor3() {
        PermutationGenerator gen = new PermutationGenerator(3);
        assertPermutation(gen.next(), new Integer[]{0,1,2});
        assertPermutation(gen.next(), new Integer[]{0,2,1});
        assertPermutation(gen.next(), new Integer[]{1,0,2});
        assertPermutation(gen.next(), new Integer[]{1,2,0});
        assertPermutation(gen.next(), new Integer[]{2,0,1});
        assertPermutation(gen.next(), new Integer[]{2,1,0});
        assertPermutation(gen.next(), null);
        try {
            gen.next();
            Assert.fail("Should fail");
        } catch (Exception e) {
            // expected
        }
    }
    
    @Test
    public void testFor4() {
        PermutationGenerator gen = new PermutationGenerator(4);
        assertPermutation(gen.next(), new Integer[]{0,1,2,3});
        assertPermutation(gen.next(), new Integer[]{0,1,3,2});
        assertPermutation(gen.next(), new Integer[]{0,2,1,3});
        assertPermutation(gen.next(), new Integer[]{0,2,3,1});
        assertPermutation(gen.next(), new Integer[]{0,3,1,2});
        assertPermutation(gen.next(), new Integer[]{0,3,2,1});
        assertPermutation(gen.next(), new Integer[]{1,0,2,3});
        assertPermutation(gen.next(), new Integer[]{1,0,3,2});
        assertPermutation(gen.next(), new Integer[]{1,2,0,3});
        assertPermutation(gen.next(), new Integer[]{1,2,3,0});
        
        // modify result array to see if it doesn't affect generator
        Integer[] permutation = gen.next();
        assertPermutation(permutation, new Integer[]{1,3,0,2});
        for (int i=0; i<4; ++i) {
            permutation[i] = i;
        }
        assertPermutation(gen.next(), new Integer[]{1,3,2,0});
    }
    
    private void assertPermutation(Integer[] actual, Integer[] expected) {
        if (actual != expected && !Arrays.equals(actual, expected)) {
            Assert.fail("Unexpected permutatoin: " + Arrays.toString(actual) + "; Expected: " + Arrays.toString(expected));
        }
    }
    
}
