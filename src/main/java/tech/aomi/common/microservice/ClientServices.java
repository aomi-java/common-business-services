package tech.aomi.common.microservice;

import org.springframework.cloud.client.ServiceInstance;

/**
 * @author Sean Create At 2019-12-13
 */
public interface ClientServices {

    <T> T newInstance(ServiceInstance instance, Class<T> clazz);


}
