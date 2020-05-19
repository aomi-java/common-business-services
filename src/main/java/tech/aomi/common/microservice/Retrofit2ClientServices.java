package tech.aomi.common.microservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import retrofit2.Retrofit;
import tech.aomi.common.web.client.retrofit2.ClientFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Sean Create At 2019/12/25
 */
@Slf4j
public class Retrofit2ClientServices implements ClientServices {

    private final DiscoveryClient discoveryClient;

    /**
     * 单位秒
     */
    private long readTimeout;

    private long writeTimeout;

    public Retrofit2ClientServices(DiscoveryClient discoveryClient) {
        this(discoveryClient, 60, 60);
    }

    public Retrofit2ClientServices(DiscoveryClient discoveryClient, long readTimeout, long writeTimeout) {
        this.discoveryClient = discoveryClient;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
    }


    private Map<String, Retrofit> cache = new HashMap<>();

    private long lastUpdateAt = System.currentTimeMillis();

    @Override
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

    @Override
    public <T> T newInstance(String name, Class<T> clazz) {
        List<String> services = discoveryClient.getServices();
        LOGGER.debug("all services: {}", services);
        if (CollectionUtils.isEmpty(services))
            return null;

        for (String serviceId : services) {
            if (serviceId.toLowerCase().contains(name.toLowerCase())) {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
                if (CollectionUtils.isEmpty(instances)) {
                    LOGGER.warn("服务{}没有运行任何实例", serviceId);
                    return null;
                }
                String baseUrl = "http://" + serviceId + "/";
                LOGGER.debug("baseUrl: {}", baseUrl);
                Retrofit retrofit = getRetrofit(baseUrl);
                return retrofit.create(clazz);
            }
        }
        return null;
    }

    private Retrofit getRetrofit(String baseUrl) {
        if ((System.currentTimeMillis() - lastUpdateAt) > (10 * 60 * 1000)) {
            cache.clear();
        }

        Retrofit retrofit = cache.get(baseUrl);
        if (null != retrofit)
            return retrofit;
        retrofit = ClientFactory.builder()
                .baseUrl(baseUrl)
                .readTimeout(this.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(this.writeTimeout, TimeUnit.SECONDS)
                .build();
        cache.put(baseUrl, retrofit);
        return retrofit;
    }


}


