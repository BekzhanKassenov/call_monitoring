package kz.edu.nu.monitored_test;

import kz.edu.nu.monitored.Monitoring;

import java.util.Arrays;
import java.util.List;

class Test_Monitored extends Test {
        
        
        void
        test2(
        )
        
        {
            List<String> callerList = Arrays.asList(
                 "kz.edu.nu.monitored_test.Main#main"  
            );
            StackTraceElement[] trace = (new Throwable()).getStackTrace();
            if (trace.length < 2) {
                throw new Error("Too short stack trace for kz.edu.nu.monitored_test.Test#test2");
            }

            String callerName = trace[1].getClassName() + "#" + trace[1].getMethodName();
            if (!callerList.contains(callerName)) {
                throw new Error("Caller " + callerName + " is not allowed for quick");
            }

            
            super.test2();
        }
        
        
        java.lang.String
        test1(
                java.lang.String a , 
                int q 
        )
        
        {
            List<String> callerList = Arrays.asList(
                 "kz.edu.nu.monitored_test.Main#main"  
            );
            StackTraceElement[] trace = (new Throwable()).getStackTrace();
            if (trace.length < 2) {
                throw new Error("Too short stack trace for kz.edu.nu.monitored_test.Test#test1");
            }

            String callerName = trace[1].getClassName() + "#" + trace[1].getMethodName();
            if (!callerList.contains(callerName)) {
                throw new Error("Caller " + callerName + " is not allowed for quick");
            }

            return 
            super.test1(a, q);
        }
}
