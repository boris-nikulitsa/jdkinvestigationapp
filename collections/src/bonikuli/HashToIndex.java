package bonikuli;

public class HashToIndex {
    
    public static void main(String[] args) {
        hash_FromHashMapJava6(123);
        hash_FromHashMapJava6(Integer.MAX_VALUE - 123);
    }
    
    public static void hash_FromHashMapJava6(final int hash) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        
        StringBuilder trace = new StringBuilder();
        trace.append("Transform hash for ").append(hash).
                append("|").append(Integer.toBinaryString(hash));
        int h = hash ^ (hash >>> 4) ^ (hash >>> 7) ^ (hash >>> 12) ^ (hash >>> 16) ^ (hash >>> 19) ^ (hash >>> 20) ^ (hash >>> 24) ^ (hash >>> 27);
        trace.append("; Result is ").append(h).
                append("|").append(Integer.toBinaryString(h));
        System.out.println(trace.toString());
    }
}
