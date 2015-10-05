package jdkinvestigationapp.jvmpermissions;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Random;

public class ReflectionPermissionApp {
    
    public static void main(String[] args) throws Exception {
        String fileName = isRunUnderSecurityManager() ? "run_under_security_manager.txt" : "run.txt";
        try (PrintWriter out = new PrintWriter(new FileOutputStream(fileName))) {
            try {
                out.println("Security manager is set: " + isRunUnderSecurityManager());

                out.println("Call Class.forName & Class.newInstance");
                InterfaceA instance = constructWithReflextion("jdkinvestigationapp.jvmpermissions.ClassA");
                out.println("New instance created");
                Random rnd = new Random();
                int callsCount = rnd.nextInt(7) + 1;
                out.println("Call action " + callsCount + " times");
                for (int i=1; i<=callsCount; ++i) {
                    instance.action();
                }
                out.println("Access public fields through reflection");
                out.println(readPublicFields(instance));
                out.println("Access public & private fields through reflection");
                out.println(readAllFields(instance));
            } catch (Exception e) {
                e.printStackTrace(out);
            }
        }
    }
    
    private static boolean isRunUnderSecurityManager() {
        return System.getSecurityManager() != null;
    }
    
    // it's part of Service Provider Interface pattern
    // so implementation is loaded on runtime
    // construction happened based on classname through reflection
    private static InterfaceA constructWithReflextion(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class clazz = Class.forName(className);
        // call default constructor
        return (InterfaceA)clazz.newInstance();
    }
    
    // sometimes we need access to private fields state
    // e.g. for logging porpouse or for advanced serialization/deserialization
    private static String readAllFields(InterfaceA instance) throws IllegalArgumentException, IllegalAccessException{
        StringBuilder privateFields = new StringBuilder();
        Class clazz = instance.getClass();
        final Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (final Field field : fields) {
            final String fieldName = field.getName();
            final Object fieldValue = field.get(instance);
            if (privateFields.length() > 0) {
                privateFields.append(", ");
            }
            privateFields.append("[").append(fieldName).append("=").append(fieldValue).append("]");
        }
        return privateFields.toString();
    }
    
    public static String readPublicFields(InterfaceA instance) throws IllegalArgumentException, IllegalAccessException {
        StringBuilder privateFields = new StringBuilder();
        Class clazz = instance.getClass();
        final Field[] fields = clazz.getFields();
        for (final Field field : fields) {
            final String fieldName = field.getName();
            final Object fieldValue = field.get(instance);
            if (privateFields.length() > 0) {
                privateFields.append(", ");
            }
            privateFields.append("[").append(fieldName).append("=").append(fieldValue).append("]");
        }
        return privateFields.toString();
    }
}
