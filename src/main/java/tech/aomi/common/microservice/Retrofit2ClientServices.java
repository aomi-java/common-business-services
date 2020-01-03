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

/**
 * @author Sean Create At 2019/12/25
 */
@Slf4j
public class Retrofit2ClientServices implements ClientServices {

    private final DiscoveryClient discoveryClient;

    public Retrofit2ClientServices(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    private Map<String, Retrofit> cache = new HashMap<>();

    private long lastUpdateAt = System.currentTimeMillis();

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
                .build();
        cache.put(baseUrl, retrofit);
        return retrofit;
    }


}


