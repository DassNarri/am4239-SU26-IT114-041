package util;

public class Helper {

    public static void publicMethod() {
        System.out.println("Public method");
    }

    protected static void protectedMethod() {
        System.out.println("Protected method");
    }

    static void defaultMethod() {
        System.out.println("Default (package-private) method");
    }

    private static void privateMethod() {
        System.out.println("Private method");
    }

    public static void accessPrivateInsideClass() {
        privateMethod();
    }
}
