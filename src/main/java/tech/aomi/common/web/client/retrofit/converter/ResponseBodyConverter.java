package tech.aomi.common.web.client.retrofit.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import tech.aomi.common.exception.ServiceException;
import tech.aomi.common.web.controller.Result;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Type;

/**
 * 收单系统响应参数转换
 *
 * @param <T>
 */
@Slf4j
final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final ObjectMapper mapper;

    private final Type returnType;

    ResponseBodyConverter(ObjectMapper mapper, Type type) {
        this.mapper = mapper;
        this.returnType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(Result.Entity.class);
            ObjectReader reader = mapper.readerFor(javaType);
            Result.Entity entity = reader.readValue(value.charStream());
            LOGGER.debug("返回结果: {}", entity);
            if (!entity.getSuccess()) {
                LOGGER.error("处理失败: {}, {}, {}", entity.getStatus(), entity.getDescribe(), entity.getPayload());
                ServiceException e = new ServiceException(entity.getDescribe());
                e.setErrorCode(entity.getStatus());
                e.setPayload((Serializable) entity.getPayload());
                throw e;
            }
            javaType = mapper.getTypeFactory().constructType(returnType);
            reader = mapper.readerFor(javaType);

            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, entity.getPayload());

            return reader.readValue(writer.toString());
        } finally {
            value.close();
        }
    }
}