package bonikuli;

import java.util.HashMap;

/**
 * That examples demonstrates issue which can happen in case 
 * hash key mutation
 */
public class HashKeyMutationIssue {
    
    private static final int JAVA_LANGUAGE_VERSION = 8;
    
    private static boolean constant_cache = false;
    private static boolean comparable_key = false;
    
    public static void main(String[] args) {
        if (JAVA_LANGUAGE_VERSION < 7 || JAVA_LANGUAGE_VERSION > 8) {
            throw new IllegalStateException("Test doesn't work for Java Language Version " + JAVA_LANGUAGE_VERSION);
        }
        (new HashKeyMutationIssue()).test1();
        (new HashKeyMutationIssue()).test2();
        if (JAVA_LANGUAGE_VERSION == 8) {
            // support for red black trees in case long hash buckets added in java 8
           (new HashKeyMutationIssue()).test3();
           (new HashKeyMutationIssue()).test4(); 
        }
    }
    
    // to be able to see state in VisualVM
    // 1. store instance as property to get it stored in the JVM heap
    // 2. store as non static to be able to locate it easily
    private final HashMap<Customer, String> cache = new HashMap<>();
    
    private void test1() {
        constant_cache = false;
        comparable_key = false;
        Customer customer1 = new Customer("Igor", "Veshner");
        Customer customer2 = new Customer("Tatiana", "Matez");
        cache.put(customer1, "ssDDss");
        cache.put(customer2, "ddSSdd");
        if (!"ssDDss".equals(cache.get(customer1)) || !"ddSSdd".equals(cache.get(customer2))) {
            throw new IllegalStateException("Everything should work fine after initilization");
        }
        // now let us change customer2 key and see what happens
        customer2.surname = customer1.surname;
// -- ISSUE REPRODUCED ---
        if (cache.containsKey(customer2)) {
            throw new IllegalStateException("Key shouldn't be found because hash changed after insertion");
        }
        // Let us change everything back
        customer2.surname = "Matez";
        if (!"ssDDss".equals(cache.get(customer1)) || !"ddSSdd".equals(cache.get(customer2))) {
            throw new IllegalStateException("Everything should work fine after we retrieved keys to original state");
        }
        
        customer2.name = customer1.name;
        customer2.surname = customer1.surname;
// -- ISSUE REPRODUCED ---
        if (!"ssDDss".equals(cache.get(customer2))) {
            throw new IllegalStateException("Key properties match first key, so algorithm should retrieve first value");
        }
    }
    
    private void test2() {
        // Let us now use implementation with constant hash code
        // we will expirience cache collision always and all data will be stored in same cache bucket
        // so issue won't appear any more in such case
        constant_cache = true;
        comparable_key = false;
        Customer customer1 = new Customer("Igor", "Veshner");
        Customer customer2 = new Customer("Tatiana", "Matez");
        cache.put(customer1, "ssDDss");
        cache.put(customer2, "ddSSdd"); // put this instance into same hash bucket as previous one
        if (!"ssDDss".equals(cache.get(customer1)) || !"ddSSdd".equals(cache.get(customer2))) {
            throw new IllegalStateException("Everything should work fine");
        }
        // now let us change customer2 key and see what happens
        customer2.surname = customer1.surname;
        if (!"ssDDss".equals(cache.get(customer1)) || !"ddSSdd".equals(cache.get(customer2))) {
            throw new IllegalStateException("Everything should work fine because hash function (is a constant) doesn't depend from key properties");
        }
        
// -- ISSUE REPRODUCED ---
        if (JAVA_LANGUAGE_VERSION == 8) {
            customer2.name = customer1.name;
            customer2.surname = customer1.surname;
            if (!"ssDDss".equals(cache.get(customer2))) {
                throw new IllegalStateException("Key properties match first key, so algorithm should retrieve first value");
            }
        
            customer2.name = "Tatiana";
            customer2.surname = "Matez";
            if (!"ssDDss".equals(cache.get(customer1)) || !"ddSSdd".equals(cache.get(customer2))) {
                throw new IllegalStateException("Everything should work fine after we retrieved keys to original state");
            }

            customer1.name = customer2.name;
            customer1.surname = customer2.surname;
// -- ATTENTION --
            if (!"ssDDss".equals(cache.get(customer1))) { 
                // logically we changed key properties, so now we expect value for the key with matched properties
                // but there's QUEUE used inside cache bucket
                // So make attention that algorithm retrieves first value (ssDDss) in both cases because it's stored before second value (ddSSdd)
                throw new IllegalStateException("Everything should work fine because even after key state change it's appear first in the hash bucket queue");
            }
        } else if (JAVA_LANGUAGE_VERSION < 8) {
            // queue is differently rotated in java 8 than in java 7, 
            // so to reproduce issue in java 7 we should do same, but for different key
            customer1.name = customer2.name;
            customer1.surname = customer2.surname;
            if (!"ddSSdd".equals(cache.get(customer2))) {
                throw new IllegalStateException("Key properties match second key, so algorithm should retrieve second value");
            }
        
            customer1.name = "Igor";
            customer1.surname = "Veshner";
            if (!"ssDDss".equals(cache.get(customer1)) || !"ddSSdd".equals(cache.get(customer2))) {
                throw new IllegalStateException("Everything should work fine after we retrieved keys to original state");
            }

            customer2.name = customer1.name;
            customer2.surname = customer1.surname;
// -- ATTENTION --
            if (!"ddSSdd".equals(cache.get(customer2))) { 
                // logically we changed key properties, so now we expect value for the key with matched properties
                // but there's QUEUE used inside cache bucket
                // So make attention that algorithm retrieves second value (ddSSdd) in both cases because it's stored before first value (ssDDss)
                throw new IllegalStateException("Everything should work fine because even after key state change it's appear first in the hash bucket queue");
            }
        }
    }
    
    private void test3() {
        constant_cache = true;
        comparable_key = false;
        // Let us now try to reproduce problem, but with Red Black Tree in cache bucket
        // With constant cache there's quarantee that we always put into same bucket 
        // From Java 8 there's red black tree used instead of queue if bucket size > HashMap.TREEIFY_THRESHOLD
        // and map capacity >= HashMap.MIN_TREEIFY_CAPACITY
        
        // There's good question. How do we use red black tree if key dosn't support comparing. 
        // Answer on this question find in the method HashMap.tieBreakOrder
        
        for (int i=1; i<=7; ++i) {
            cache.put(new Customer("Name #" + i, "Surname #" + i), "Value #" + i);
        }
        cache.put(new Customer("Name #8", "Surname #8"), "Value #8"); // resize from 2^3 to 2^4 because capacity less than HashMap.MIN_TREEIFY_CAPACITY
        cache.put(new Customer("Name #9", "Surname #9"), "Value #9"); // resize from 2^4 to 2^5 because capacity less than HashMap.MIN_TREEIFY_CAPACITY
        Customer customer1 = new Customer("Name #10", "Surname #10");
        cache.put(customer1, "Value #10"); // resize from 2^5 to 2^6 because capacity less than HashMap.MIN_TREEIFY_CAPACITY
        // HashMap.MIN_TREEIFY_CAPACITY reached
        if (!"Value #10".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
        
        customer1.surname = "XXX";
        if (!"Value #10".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine becaue there's still queue used in the hash bucket");
        }
        customer1.surname = "Surname #10"; // change value back
        if (!"Value #10".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }

        customer1 = new Customer("Name #11", "Surname #11");
        cache.put(customer1, "Value #11"); // hash bucket rebuild from queue to red black tree !
        if (!"Value #11".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
        
        int identityHashCodeBeforePropUpdate = System.identityHashCode(customer1);
        customer1.surname = "AAA";
        if (identityHashCodeBeforePropUpdate != System.identityHashCode(customer1)) {
            throw new IllegalStateException("[subjective view] I saw that instance state changes didn't affect identity hash code generated by my JVM");
        }
        System.out.println("-- Constant Cache Case : After Red Black Tree is build : Key Value Changed --");
        System.out.println(cache.get(customer1));
        if (!"Value #11".equals(cache.get(customer1))) {
            throw new IllegalStateException("[subjective view] I saw that everything works fine. I think that it's because of how identity hash code is evaluated");
        }
        
        customer1.surname = "Surname #11"; // change value back
        if (!"Value #11".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
    }
    
    private void test4() {
        constant_cache = true;
        comparable_key = true;
        
        for (int i=1; i<=9; ++i) {
            cache.put(new Customer("Name #" + i, "Surname #" + i), "Value #" + i);
        }
        Customer customer1 = new Customer("Name #10", "Surname #10");
        cache.put(customer1, "Value #10");
        if (!"Value #10".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
        customer1.name = "a";
        customer1.surname = "b";
        if (!"Value #10".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine because hash bucket structure is still queue");
        }
        customer1.name = "Name #10";
        customer1.surname = "Surname #10";
        if (!"Value #10".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
        
        customer1 = new Customer("Name #11", "Surname #11");
        cache.put(customer1, "Value #11"); // bucket queue rebuild to red black tree !
        if (!"Value #11".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
        
        customer1.name = "a";
        customer1.surname = "b";
// -- ISSUE REPRODUCED --
        if (cache.containsKey(customer1)) {
            throw new IllegalStateException("Key shouldn't be found because it's expected to be in different red black tree branch");
        }
        
        customer1.name = "Name #11";
        customer1.surname = "Surname #11";
        if (!"Value #11".equals(cache.get(customer1))) {
            throw new IllegalStateException("Everything should work fine");
        }
    }
        
    
    public static class Customer implements Comparable<Customer> {
        private String name, surname;
        
        public Customer(String name, String surname) {
            if (name == null) {
                throw new NullPointerException("name is null");
            }
            if (surname == null) {
                throw new NullPointerException("surname is null");
            }
            this.name = name;
            this.surname = surname;
        }

        @Override
        public int hashCode() {
            return constant_cache ? 0 : (name + surname).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Customer)) {
                return false;
            }
            Customer other = (Customer)obj;
            return name.equals(other.name) && surname.equals(other.surname);
        }

        @Override
        public int compareTo(Customer o) {
            if (!comparable_key) {
                return 0;
            }
            return (name + surname).compareTo(o.name + o.surname);
        }
    }
}
