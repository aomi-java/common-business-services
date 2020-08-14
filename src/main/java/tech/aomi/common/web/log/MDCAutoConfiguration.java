package tech.aomi.common.web.log;

import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MDCAutoConfiguration {

    @Bean(name = {"applicationTaskExecutor", "taskExecutor"})
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        ThreadPoolTaskExecutor executor = builder.build();
        executor.setTaskDecorator(new MDCTaskDecorator());
        return executor;
    }

}
