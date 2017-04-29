package kz.edu.nu.monitored_test;

import kz.edu.nu.monitored.annotations.CallStack;
import kz.edu.nu.monitored.annotations.CalledBy;
import kz.edu.nu.monitored.annotations.Monitored;

@Monitored
class Test {

    static Test createInstance() {
        return new Test_Monitored();
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