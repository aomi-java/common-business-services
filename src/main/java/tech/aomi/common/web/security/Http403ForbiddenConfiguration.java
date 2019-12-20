package tech.aomi.common.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

/**
 * 未经身份验证的用户发生403错误，无权限时处理方法
 * 403 无权限访问结果处理
 *
 * @author Sean Create At 2019/12/19
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurer.class)
public class Http403ForbiddenConfiguration {

    @Bean
    public Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenImpl();
    }

    @Order(Integer.MAX_VALUE - 1)
    @Configuration
    public static class AccessDeniedHandlerConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired(required = false)
        private Http403ForbiddenEntryPoint http403ForbiddenEntryPoint;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            if (null != http403ForbiddenEntryPoint) {
                http.exceptionHandling()
                        .authenticationEntryPoint(http403ForbiddenEntryPoint);
            }
        }

    }

}
