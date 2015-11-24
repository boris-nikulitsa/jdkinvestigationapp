package bonikuli;

import java.util.HashMap;
import java.util.Random;

public class HashCollision {
    
    // Map table will contain table of size 2048 because of rehashing algorithm,
    // but most interesting is that only 2047 entries in table are null and only 1 points
    // to red black tree. The tree contains all 1000 elements as nodes.
    private final HashMap<InvalidHashImplCl, Integer> map = new HashMap<>();
    
    public static void main(String[] args) {
        HashCollision test = new HashCollision();
        Random rnd = new Random();
        for (int i=0; i<1000; ++i) {
            int state = rnd.nextInt();
            test.map.put(new InvalidHashImplCl(state), state);
        }
    }
    
    
    private static class InvalidHashImplCl {

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InvalidHashImplCl other = (InvalidHashImplCl) obj;
            if (this.state != other.state) {
                return false;
            }
            return true;
        }
        
        private int state;
        
        public InvalidHashImplCl(int state) {
            this.state = state;
        }
        
        
    }
    
}
