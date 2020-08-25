package tech.aomi.common.web.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.aomi.common.constant.HttpHeader;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Configuration
public class LogIdAutoConfiguration extends OncePerRequestFilter {

    private static final String ID = "logId";

    private static final String START_AT = "START_AT";

    @Bean(name = {"applicationTaskExecutor", "taskExecutor"})
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        ThreadPoolTaskExecutor executor = builder.build();
        executor.setTaskDecorator(new MDCTaskDecorator(ID));
        return executor;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);

        if (isFirstRequest) {
            long start = System.currentTimeMillis();
            String reqId = request.getHeader(HttpHeader.REQUEST_ID);
            if (null == reqId || reqId.isEmpty()) {
                reqId = UUID.randomUUID().toString().replaceAll("-", "");
            }
            MDC.put(ID, reqId);
            LOGGER.debug("请求处理开始: {}, {}", start, request.getRequestURI());
            MDC.put(START_AT, start + "");
            response.setHeader(HttpHeader.REQUEST_ID, reqId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            afterCompletionProcess(request);
        }
    }

    /**
     * The default value is "false" so that the filter may log a "before" message
     * at the start of request processing and an "after" message at the end from
     * when the last asynchronously dispatched thread is exiting.
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    private void afterCompletionProcess(HttpServletRequest request) {
        if (isAsyncStarted(request)) {
            return;
        }

        String startStr = MDC.get(START_AT);
        long start = 0L;
        long end = System.currentTimeMillis();
        if (null == startStr || startStr.isEmpty()) {
            return;
        }
        try {
            start = Long.parseLong(MDC.get(START_AT));
        } catch (Exception ignored) {
        }
        LOGGER.debug("请求处理结束: {}, 耗时: {}, {}", end, end - start, request.getRequestURI());
        MDC.remove(START_AT);
        MDC.remove(ID);
    }
}
