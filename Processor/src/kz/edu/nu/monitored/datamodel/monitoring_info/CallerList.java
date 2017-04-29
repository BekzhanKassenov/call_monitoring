package kz.edu.nu.monitored.datamodel.monitoring_info;

import kz.edu.nu.monitored.annotations.CalledBy;

import javax.lang.model.element.ExecutableElement;
import java.util.Arrays;
import java.util.List;

public class CallerList extends MonitoringInfo {
    private List<String> callerList;

    CallerList(ExecutableElement element) {
        super(Type.CALLER_LIST);
        callerList = Arrays.asList(element.getAnnotation(CalledBy.class).value());
    }

    public List<String> getCallers() {
        return callerList;
    }

    @Override
    public String getTemplateFile() {
        return "monitoring_body/CallerList.ftl";
    }
}
