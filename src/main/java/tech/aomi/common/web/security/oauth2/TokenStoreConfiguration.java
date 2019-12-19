package tech.aomi.common.web.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Oauth TokenStore 配置
 *
 * @author Sean Create At 2019/12/19
 */
@Configuration
@ConditionalOnClass({ResourceServerConfigurerAdapter.class})
public class TokenStoreConfiguration {

    @Configuration
    @ConditionalOnClass(RedisConnectionFactory.class)
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

    @Configuration
    public static class TokenStoreConfigurerAdapter extends ResourceServerConfigurerAdapter {

        @Autowired(required = false)
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            if (null != tokenStore)
                resources.tokenStore(tokenStore);
        }
    }

}
