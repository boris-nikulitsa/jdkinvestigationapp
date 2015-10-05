package jdkinvestigationapp.jvmpermissions;


public class ClassA implements InterfaceA {
    
    private int callNumber;
    public int factorial = 1;
    public int arithmeticProgressionSum = 0;
    
    public ClassA() {
    }
    
    @Override
    public void action() {
        callNumber++;
        factorial *= callNumber;
        arithmeticProgressionSum += callNumber;
    }
    
}
