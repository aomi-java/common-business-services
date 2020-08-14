package tech.aomi.common.web.log;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;
import java.util.UUID;

public class MDCTaskDecorator implements TaskDecorator {

    private static final String ID = "logId";

    @Override
    public Runnable decorate(Runnable runnable) {
        if (null == MDC.get(ID)) {
            MDC.put(ID, UUID.randomUUID().toString().replaceAll("-", ""));
        }
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
