package tech.aomi.common.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 自定义访问策略处理
 * 用户需要自己实现{@link SecurityServices} 接口
 *
 * @author Sean Create At 2019/12/19
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurer.class)
public class AccessDecisionVoterConfiguration {

    @Bean
    @ConditionalOnMissingBean(SecurityServices.class)
    public SecurityServices securityServices() {
        return new SecurityServices() {
        };
    }

    @Order(9999)
    @Configuration
    public static class AccessDecisionVoterConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired(required = false)
        private SecurityServices securityServices;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.securityContext()
                    .withObjectPostProcessor(new ObjectPostProcessor<AffirmativeBased>() {

                        @Override
                        public <O extends AffirmativeBased> O postProcess(O object) {
                            if (null != securityServices)
                                object.getDecisionVoters().add(new AccessDecisionVoterImpl(securityServices));
                            return object;
                        }
                    });
        }
    }
}
