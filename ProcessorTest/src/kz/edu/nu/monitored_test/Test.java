package kz.edu.nu.monitored_test;

import kz.edu.nu.monitored.annotations.CalledBy;
import kz.edu.nu.monitored.annotations.Monitored;

@Monitored
public class Test {

    public static Test createInstance() {
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
     * Non-void return type, non-empty paramenter list
     */
    @CalledBy({
            "kz.edu.nu.monitored_test.Main#main"
    })
    String test1(String a, int q) {
        System.out.println(q);
        return a;
    }
}