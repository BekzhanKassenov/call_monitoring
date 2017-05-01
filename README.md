# Project
Call monitoring is annotation processor used for allowing/disallowing certain callers for some particular method in runtime.

# Build
TODO

# What's inside?
Call monitoring generates subclass that must be always returned as an instance of monitored class. Generated sublcass verifies caller and then delegates to superclass.

# Limitations
The way monitoring is performed creates some limitation on the way we write the class.

1. Class should not be instantiated directly. Instead use static method which will return instance of subclass.
2. Class cannot be final. We should be able to inherit from it.
3. Monitored methods should not be abstract, static or final.
4. There's no support for generics (for now).

# How to use it?
Call monitoring provides three annotations for use. They are:

1. `@Monitored`. Used for classes that contain methods to be monitored.
2. `@CalledBy(<list of several method's FQN>)`. Used to allow only given callers for annotated method.
3. `@CallStack(<list of call stack items>)`. Allow only given chain of callers at the moment of call.

## Example
###### MonitoredClass.java
    package com.example.test;

    import kz.edu.nu.monitored.annotations.CalledBy;
    import kz.edu.nu.monitored.annotations.Monitored;

    @Monitored
    class MonitoredClass {
        static MonitoredClass getInstance() {
            return new MonitoredClass_Monitored();
        }

        @CalledBy("com.example.test.Main# allowedCaller")
        int call(String s, int q) {
            System.out.println(s);
            return q;
        }
    }

###### Main.java
    package com.example.test;

    public static void main(String args[]) {
        allowedCaller();
        try {
            MonitoredClass cl = MonitoredClass.getInstance();
            System.out.println(cl.call("Hello", 1));
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    static void allowedCaller() {
        MonitoredClass cl = MonitoredClass.getInstance();
        System.out.println(cl.call("Hello", 1));
    }

### MonitoredClass_Monitored.java (after reformatting)
    package com.example.test;

    import kz.edu.nu.monitored.Monitoring;

    import java.util.Arrays;
    import java.util.List;

    class MonitoredClass_Monitored extends MonitoredClass {

        MonitoredClass_Monitored() {
            super();
        }

        int call(java.lang.String s, int q) {
            String[] callerList = {
                    "com.example.test.Main# allowedCaller"
            };
            StackTraceElement[] trace = (new Throwable()).getStackTrace();
            if (!Monitoring.verifyCallerList(callerList, trace)) {
                throw new Error("Caller " + trace[1].getMethodName() + " is not allowed for call");
            }

            return super.call(s, q);
        }
    }

# Notes for contributors:
### Steps for adding new monitoring type (like `@CallStack` or `@CalledBy`):

1. Create annotation for your monitoring type.
2. Create subclass of MonitoringInfo.java, add Type enum (which is used as factory).
3. Create template for your monitoring body.

### Other notes:

1. Templates are written using Freemarker.
2. `MonitoredProcessor.java` contains the logic of extraction data from source code.
2. Data model for constructors and methods is stored in `ExecutableModel` class.
