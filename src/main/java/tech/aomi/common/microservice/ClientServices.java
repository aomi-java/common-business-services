package tech.aomi.common.microservice;

import org.springframework.cloud.client.ServiceInstance;

/**
 * @author Sean Create At 2019-12-13
 */
public interface ClientServices {

    <T> T newInstance(ServiceInstance instance, Class<T> clazz);

    /**
     * 获取一个随机实例
     *
     * @param name 服务名称
     * @return 服务实例
     */
    ServiceInstance getRandomInstance(String name);

}
