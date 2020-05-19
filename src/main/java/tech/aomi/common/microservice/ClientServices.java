package tech.aomi.common.microservice;

import java.util.List;

/**
 * @author Sean Create At 2019-12-13
 */
public interface ClientServices {

    List<String> getServices();

    /**
     *
     * @param name 服务名称
     * @param clazz 服务实例class
     * @param <T> 服务类型
     * @return 服务实例
     */
    <T> T newInstance(String name, Class<T> clazz);


}
