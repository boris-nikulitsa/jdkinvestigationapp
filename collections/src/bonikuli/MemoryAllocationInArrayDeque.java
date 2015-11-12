package bonikuli;

public class MemoryAllocationInArrayDeque {
    
    public static void main(String[] args) {
        allocateElements(13);
        allocateElements(Integer.MAX_VALUE / 2 - 3);
        allocateElements(Integer.MAX_VALUE - 1);
        
        int head = 0;
        for (int i=0; i<3; i++) {
            head = moveHead(head, 8, true);
        }
        for (int i=0; i<3; i++) {
            head = moveHead(head, 8, false);
        }
    }
    
    private static int moveHead(final int head, final int capacity, final boolean add) {
        int newHead = add ? ((head - 1) & (capacity - 1)) : ((head + 1) & (capacity - 1));
        StringBuilder trace = new StringBuilder();
        trace.append("Move head=").append(head).
                append(" capacity=").append(capacity).
                append(" add=").append(add).
                append(" => new head=").append(newHead);
        System.out.println(trace.toString());
        return newHead;
    }
    
    /*
    Here's algorithm of capacity allocation used in ArrayDeque
    */
    private static void allocateElements(int numElements) {
        StringBuilder trace = new StringBuilder();
        trace.append("Converting ").append(numElements).append("|").append(Integer.toBinaryString(numElements));
        int initialCapacity = 8;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            if (initialCapacity >>>  1 != 0) {
                trace.append("; OR with ").append(Integer.toBinaryString(initialCapacity >>>  1));
                initialCapacity |= (initialCapacity >>>  1);
                trace.append("=>").append(Integer.toBinaryString(initialCapacity));
            }
            if (initialCapacity >>>  2 != 0) {
                trace.append("; OR with ").append(Integer.toBinaryString(initialCapacity >>>  2));
                initialCapacity |= (initialCapacity >>>  2);
                trace.append("=>").append(Integer.toBinaryString(initialCapacity));
            }
            if (initialCapacity >>>  4 != 0) {
                trace.append("; OR with ").append(Integer.toBinaryString(initialCapacity >>>  4));
                initialCapacity |= (initialCapacity >>>  4);
                trace.append("=>").append(Integer.toBinaryString(initialCapacity));
            }
            if (initialCapacity >>>  8 != 0) {
                trace.append("; OR with ").append(Integer.toBinaryString(initialCapacity >>>  8));
                initialCapacity |= (initialCapacity >>>  8);
                trace.append("=>").append(Integer.toBinaryString(initialCapacity));
            }
            if (initialCapacity >>>  16 != 0) {
                trace.append("; OR with ").append(Integer.toBinaryString(initialCapacity >>>  16));
                initialCapacity |= (initialCapacity >>>  16);
                trace.append("=>").append(Integer.toBinaryString(initialCapacity));
            }
            trace.append("; capacity after bitwise operations is ").append(initialCapacity);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        trace.append("; result=").append(initialCapacity).append("|").append(Integer.toBinaryString(initialCapacity));
        System.out.println(trace.toString());
    }
}
