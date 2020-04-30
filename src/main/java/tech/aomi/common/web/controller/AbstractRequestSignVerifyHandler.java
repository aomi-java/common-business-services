package tech.aomi.common.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2018/6/12
 */
@Slf4j
public abstract class AbstractRequestSignVerifyHandler extends RequestBodyAdviceAdapter {

    public static final int EOF = -1;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpProperties properties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        EnableSignature typeEnableSignature = methodParameter.getContainingClass().getAnnotation(EnableSignature.class);
        if (null != typeEnableSignature)
            return true;
        EnableSignature enableSignature = methodParameter.getMethodAnnotation(EnableSignature.class);
        return null != enableSignature;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        byte[] buffer = new byte[1024 * 1024];
        int len = read(inputMessage.getBody(), buffer);

        byte[] body = ArrayUtils.subarray(buffer, 0, len);

        verify(body);

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(body);
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    protected abstract void verify(byte[] body);

    private int read(InputStream input, byte[] buffer) throws IOException {
        int remaining = buffer.length;
        while (remaining > 0) {
            final int location = buffer.length - remaining;
            final int count = input.read(buffer, location, remaining);
            if (EOF == count) { // EOF
                break;
            }
            remaining -= count;
        }
        return buffer.length - remaining;
    }
}
