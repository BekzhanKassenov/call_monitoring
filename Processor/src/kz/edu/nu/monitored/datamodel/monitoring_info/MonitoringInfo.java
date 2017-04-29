package kz.edu.nu.monitored.datamodel.monitoring_info;

import com.google.common.collect.ImmutableMap;
import kz.edu.nu.monitored.annotations.CallStack;
import kz.edu.nu.monitored.annotations.CalledBy;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class MonitoringInfo {
    public enum Type {
        NO_MONITORING {
            @Override
            public MonitoringInfo getInstance(ExecutableElement unused) {
                return new NoMonitoring();
            }
        },
        CALLER_LIST {
            @Override
            public MonitoringInfo getInstance(ExecutableElement element) {
                return new CallerList(element);
            }
        },
        CALLER_CHAIN {
            @Override
            public MonitoringInfo getInstance(ExecutableElement element) {
                return new CallerChain(element);
            }
        }
        ;

        public abstract MonitoringInfo getInstance(ExecutableElement element);
    }


    public static final
        Map<Class<? extends Annotation>, Type> MONITORING_TYPES =
            ImmutableMap.of(
                CalledBy.class, Type.CALLER_LIST,
                CallStack.class, Type.CALLER_CHAIN);

    public static final Set<Class<? extends Annotation>> MONITORING_ANNOTATIONS =
            MONITORING_TYPES.keySet();

    private Type monitoringType;

    MonitoringInfo(Type monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getMonitoringType() {
        return monitoringType.toString();
    }

    public abstract String getTemplateFile();

    public static MonitoringInfo from(ExecutableElement element) throws Exception {
        Set<Type> monitoringTypes = new HashSet<>();
        for (Class <? extends Annotation> annotationClass : MONITORING_ANNOTATIONS) {
            if (element.getAnnotation(annotationClass) != null) {
                monitoringTypes.add(MONITORING_TYPES.get(annotationClass));
            }
        }

        // There're three options for size of monitoringTypes:
        // 1) Its size is greater than one. In that case we have 2 clashing
        // annotation. This is error.
        if (monitoringTypes.size() > 1) {
            throw new Exception(
                String.format(
                    "Several types of monitoring are specified for %s",
                    element.getSimpleName()));
        }

        // 2) Its empty. In that case we provide no monitoring. OK case.
        if (monitoringTypes.isEmpty()) {
            return Type.NO_MONITORING.getInstance(element);
        }

        // 3) It's size is exactly one. We have the only annotation OK case.
        return monitoringTypes.iterator().next().getInstance(element);
    }
}
