package tech.aomi.common.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

    @Order(99999)
    @Configuration
    public static class AccessDeniedHandlerConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired(required = false)
        private AccessDeniedHandler accessDeniedHandler;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            if (null != accessDeniedHandler) {
                http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
            }
        }

    }

}
