package tech.aomi.common.microservice;

import java.util.List;

/**
 * @author Sean Create At 2019-12-13
 */
public interface ClientServices {

    <T> T getServices(Class<T> clazz);

    /**
     * @return 获取所有服务名称
     */
    List<String> getServiceNames();


}
