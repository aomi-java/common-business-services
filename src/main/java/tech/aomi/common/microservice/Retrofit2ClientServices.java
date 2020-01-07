package tech.aomi.common.microservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import retrofit2.Retrofit;
import tech.aomi.common.web.client.retrofit2.ClientFactory;

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
    public <T> T newInstance(ServiceInstance instance, Class<T> clazz) {
        String baseUrl = instance.getScheme() + instance.getHost() + "/";
        LOGGER.debug("baseUrl: {}", baseUrl);
        Retrofit retrofit = getRetrofit(baseUrl);
        return retrofit.create(clazz);
    }

    /**
     * 模糊匹配服务名字，找到对应的服务
     *
     * @param name 服务名称
     * @return 服务实例
     */
    @Override
    public ServiceInstance getRandomInstance(String name) {
        List<String> services = discoveryClient.getServices();
        LOGGER.debug("all services: {}", services);
        if (CollectionUtils.isEmpty(services))
            return null;

        for (String serviceId : services) {
            if (serviceId.toLowerCase().contains(name.toLowerCase())) {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
                if (CollectionUtils.isEmpty(instances)) {
                    LOGGER.error("服务{}没有运行任何实例", serviceId);
                    return null;
                }
                LOGGER.debug("随机获取一个服务实例");
                Random random = new Random();
                int index = random.nextInt(instances.size());
                ServiceInstance instance = instances.get(index);
                LOGGER.debug("instanceId: {}, serviceId: {}, host: {}, port: {}, uri: {}, metadata: {}",
                        instance.getInstanceId(),
                        instance.getServiceId(),
                        instance.getHost(),
                        instance.getPort(),
                        instance.getUri(),
                        instance.getMetadata()
                );
                return instance;
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


