package tech.aomi.common.microservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import retrofit2.Retrofit;
import tech.aomi.common.web.client.retrofit2.ClientFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sean Create At 2019/12/25
 */
@Slf4j
public class Retrofit2ClientServices implements ClientServices {

    private Map<String, Retrofit> cache = new HashMap<>();

    private long lastUpdateAt = System.currentTimeMillis();

    @Override
    public <T> T newInstance(ServiceInstance instance, Class<T> clazz) {
        String baseUrl = instance.getScheme() + instance.getHost();
        Retrofit retrofit = getRetrofit(baseUrl);
        return retrofit.create(clazz);
    }

    private Retrofit getRetrofit(String baseUrl) {
        if ((System.currentTimeMillis() - lastUpdateAt) > (10 * 60 * 1000)) {
            cache.clear();
        }

        Retrofit retrofit = cache.get(baseUrl);
        if (null != retrofit)
            return retrofit;
        retrofit = ClientFactory.builder().build();
        cache.put(baseUrl, retrofit);
        return retrofit;
    }


}


