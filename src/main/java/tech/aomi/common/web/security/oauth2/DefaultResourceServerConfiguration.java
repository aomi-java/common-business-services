package tech.aomi.common.web.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Sean Create At 2019/12/20
 */
@Configuration
@ConditionalOnClass(ResourceServerConfigurerAdapter.class)
public class DefaultResourceServerConfiguration {

    /**
     * OAuth2Exception异常处理服务
     */
    @Bean
    @ConditionalOnProperty(name = "bs.oauth2.jsonExceptionRenderer", matchIfMissing = true)
    public OAuth2ExceptionRendererImpl oAuth2ExceptionRendererImpl() {
        return new OAuth2ExceptionRendererImpl();
    }

    @Configuration
    @ConditionalOnClass(RedisConnectionFactory.class)
    @ConditionalOnProperty(name = "bs.oauth2.redisTokenStore", matchIfMissing = true)
    public static class RedisTokenStoreConfiguration {

        /**
         * 声明TokenStore实现
         */
        @Bean
        @Autowired
        @ConditionalOnMissingBean(TokenStore.class)
        @ConditionalOnBean(RedisConnectionFactory.class)
        public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
            return new RedisTokenStore(redisConnectionFactory);
        }

    }
}
