package tech.aomi.common.web.interceptor;

import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractHandlerInterceptor implements AsyncHandlerInterceptor {

    private static final String KEY = "AsyncHttpRequest";

    @Override
    public final void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(KEY, true);
        handleAfterConcurrentHandlingStarted(request, response, handler);
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == request.getAttribute(KEY))
            return handlePreHandle(request, response, handler);
        return true;
    }

    protected void handleAfterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    }

    protected boolean handlePreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
}
