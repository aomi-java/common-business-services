package tech.aomi.common.web.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tech.aomi.common.constant.HttpHeader;
import tech.aomi.common.web.interceptor.AbstractHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 每次请求生成一个唯一的请求ID。用于日志追踪
 * 请求开始和结束时的时间
 *
 * @author Sean Create At 2020/4/15
 */
@Slf4j
@Component
public class LogInterceptor extends AbstractHandlerInterceptor {

    /**
     * 日志跟踪标识
     */
    private static final String ID = "logId";

    @Override
    public boolean handlePreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String reqId = request.getHeader(HttpHeader.REQUEST_ID);
        if (StringUtils.isEmpty(reqId))
            reqId = UUID.randomUUID().toString().replaceAll("-", "");

        response.setHeader(HttpHeader.REQUEST_ID, reqId);
        MDC.put(ID, reqId);
        LOGGER.debug("请求处理开始: {}", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOGGER.debug("请求处理结束: {}", System.currentTimeMillis());
        MDC.remove(ID);
    }

}
