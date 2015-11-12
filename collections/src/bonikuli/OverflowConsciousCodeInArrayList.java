package bonikuli;

public class OverflowConsciousCodeInArrayList {
    
    public static void main(String[] args) {
        /*
        in Java 6 ArrayList is reallocated with formula 
        <new capacity> = (<capacity> * 3) / 2 + 1
        
        in Java 7 ArrayList capacity increased based on formula 
        <new capacity> = <capacity> + (<capacity> >> 1)
        */
        int java6 = 10;
        int java7 = 10;
        int iteration = 0;
        while (iteration < 50) {
            iteration++;
            int nextVal = (java6 * 3) / 2 + 1;
            java6 = nextVal < java6 ? java6 + 1 : nextVal;
            nextVal = java7 + (java7 >> 1);
            java7 = nextVal < java7 ? java7 + 1 : nextVal;
            System.out.println("Reallocation #" + iteration + ") In Java 6 ArrayList capacity will be " + java6 + "; in Java 7 ArrayList capacity will be " + java7);
        }
    }
    
}
