package tech.aomi.common.microservice.k8s;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import tech.aomi.common.microservice.ClientServices;

import java.util.List;

/**
 * @author Sean Create At 2019-12-13
 */
public class K8sClientServices implements ClientServices {

    private DiscoveryClient discoveryClient;

    @Override
    public <T> T getServices(Class<T> clazz) {
        return null;
    }

    @Override
    public List<String> getServiceNames() {
        return null;
    }
}
