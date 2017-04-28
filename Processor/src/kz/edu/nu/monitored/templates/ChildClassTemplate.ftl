package ${classPackage};

import kz.edu.nu.monitored.Monitoring;

import java.util.Arrays;
import java.util.List;

class ${className}_Monitored extends ${className} {
    <#list monitoredMethods as method>
        <#include "MethodHeader.ftl"> <#-- Setup header of the method -->
        {
            <#include "monitoring_bodies/CalledBy.ftl">
        }
    </#list>
} <#-- End of class -->
