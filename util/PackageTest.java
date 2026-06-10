package util;

public class PackageTest {

    public static void main(String[] args) {

        Helper.publicMethod();      // OK
        Helper.protectedMethod();   // OK
        Helper.defaultMethod();     // OK

        Helper.privateMethod();  // ERROR

    }
}
