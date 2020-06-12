package tech.aomi.common.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tech.aomi.common.constant.HttpHeader;

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
public class LogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 日志跟踪标识
     */
    private static final String ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String reqId = request.getHeader(HttpHeader.REQUEST_ID);
        response.setHeader(HttpHeader.REQUEST_ID, reqId);
        if (StringUtils.isEmpty(reqId))
            reqId = UUID.randomUUID().toString().replaceAll("-", "");
        if (StringUtils.isEmpty(MDC.get(ID))) {
            MDC.put(ID, reqId);
        }
        LOGGER.debug("请求处理开始: {}", System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        LOGGER.debug("请求处理结束: {}", System.currentTimeMillis());
        MDC.remove(ID);
    }

}
