package app;

import util.Helper;

public class ChildHelper extends Helper {

    public static void main(String[] args) {

        publicMethod();

        protectedMethod();   // OK (inherited)

        defaultMethod();

        privateMethod();
    }
}
