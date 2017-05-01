package kz.edu.nu.monitored_test;

import kz.edu.nu.monitored.annotations.CallStack;
import kz.edu.nu.monitored.annotations.CalledBy;
import kz.edu.nu.monitored.annotations.Monitored;

@Monitored
class Test {

    Test(String s) {
        System.out.println(s);
    }

    Test() {}

    static Test createInstance() {
        return new Test_Monitored();
    }

    static Test createInstance(String s) {
        return new Test_Monitored(s);
    }

    /**
     * Empty parameter list, void return type
     */
    @CalledBy({
            "kz.edu.nu.monitored_test.Main#main"
    })
    void test2() {

    }

    /**
     * Non-void return type, non-empty parameter list
     */
    @CalledBy({
            "kz.edu.nu.monitored_test.Main#main"
    })
    String test1(String a, int q) {
        System.out.println(q);
        return a;
    }

    @CallStack({
            "kz.edu.nu.monitored_test.Main#testLayer",
            "kz.edu.nu.monitored_test.Main#main"
    })
    String test3(String a, int q) {
        System.out.println(q);
        return a;
    }
}