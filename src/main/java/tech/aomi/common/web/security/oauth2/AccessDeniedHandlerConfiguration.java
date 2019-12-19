package tech.aomi.common.web.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 访问被拒绝响应结果处理
 * 403 无权限访问结果处理
 *
 * @author Sean Create At 2019/12/19
 */
@Configuration
@ConditionalOnClass(AccessDeniedHandler.class)
public class AccessDeniedHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Configuration
    public static class AccessDeniedHandlerConfigurerAdapter extends ResourceServerConfigurerAdapter {

        @Autowired(required = false)
        private AccessDeniedHandler accessDeniedHandler;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            if (null != accessDeniedHandler) {
                resources.accessDeniedHandler(accessDeniedHandler);
            }
        }

    }

}
