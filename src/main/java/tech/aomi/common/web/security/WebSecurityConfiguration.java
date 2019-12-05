package tech.aomi.common.web.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * web 安全相关配置
 *
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2018/7/16
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurer.class)
public class WebSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(SecurityServices.class)
    public SecurityServices securityServices() {
        return new SecurityServices() {
        };
    }

    /**
     * 无权限异常处理
     */
    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }
}
