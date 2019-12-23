package tech.aomi.common.web.security.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
    @ConditionalOnProperty(name = "bs.oauth2.tokenStoreType", havingValue = "jwt", matchIfMissing = true)
    public static class JwtTokenStoreConfiguration {

        @Value("bs.oauth2.jwt.publicKey")
        private String publicKey;

        @Value("bs.oauth2.jwt.privateKey")
        private String privateKey;

        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        protected JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            if (StringUtils.isNotEmpty(publicKey)) {
                converter.setVerifierKey(publicKey);
            }
            if (StringUtils.isNotEmpty(privateKey)) {
                converter.setSigningKey(privateKey);
            }
            return converter;
        }

    }
}
