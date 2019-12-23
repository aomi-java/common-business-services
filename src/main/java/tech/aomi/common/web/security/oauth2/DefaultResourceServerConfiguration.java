package tech.aomi.common.web.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StringUtils;

/**
 * @author Sean Create At 2019/12/20
 */
@Configuration
@ConditionalOnClass(ResourceServerConfigurerAdapter.class)
public class DefaultResourceServerConfiguration {

    @Autowired(required = false)
    private ResourceServerProperties resourceServerProperties;

    @Autowired(required = false)
    private AuthorizationServerProperties authorizationServerProperties;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        String publicKey = "";
        if (null != resourceServerProperties)
            publicKey = resourceServerProperties.getJwt().getKeyValue();
        String privateKey = "";
        if (null != authorizationServerProperties)
            privateKey = authorizationServerProperties.getJwt().getKeyValue();

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        if (!StringUtils.isEmpty(publicKey)) {
            converter.setVerifierKey(publicKey);
            converter.setVerifier(new RsaVerifier(publicKey));
        }

        if (!StringUtils.isEmpty(privateKey)) {
            converter.setSigningKey(privateKey);
        }
        return converter;
    }

    @Bean
    public DefaultTokenServices jwtTokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(jwtTokenStore());
        return services;
    }

    /**
     * OAuth2Exception异常处理服务
     */
    @Bean
    @ConditionalOnProperty(name = "bs.oauth2.jsonExceptionRenderer", matchIfMissing = true)
    public OAuth2ExceptionRendererImpl oAuth2ExceptionRendererImpl() {
        return new OAuth2ExceptionRendererImpl();
    }

}
