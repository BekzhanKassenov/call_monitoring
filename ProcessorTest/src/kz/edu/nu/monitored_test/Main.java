package kz.edu.nu.monitored_test;

public class Main {
    public static void main(String[] args) {
        Test test = Test.createInstance();

        // Testing from main
        System.out.println(test.test1("a", 0));
        test.test2();
        
        testOutsideMain(test);

        try {
            System.out.println(test.test3("a", 0));
            System.err.println("Exception was not thrown while testing test1!");
            System.exit(1);
        } catch (Throwable t) {
            // OK
        }

        testLayer(test);
    }

    static void testLayer(Test test) {
        System.out.println(test.test3("a", 0));
    }

    static void testOutsideMain(Test test) {

        try {
            System.out.println(test.test1("b", 1));
            System.err.println("Exception was not thrown while testing test1!");
            System.exit(1);
        } catch (Throwable t) {
            // Ok, should be thrown
        }

        try {
            test.test2();
            System.err.println("Exception was not thrown while testing test2!");
            System.exit(1);
        } catch (Throwable t) {
            // Ok, should be thrown
        }
    }
}
