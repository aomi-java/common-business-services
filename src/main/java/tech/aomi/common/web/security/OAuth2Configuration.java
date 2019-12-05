package tech.aomi.common.web.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;

/**
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2018/7/19
 */
@Configuration
@ConditionalOnClass(OAuth2AuthenticationEntryPoint.class)
public class OAuth2Configuration {

    /**
     * OAuth2Exception异常处理服务
     */
    @Bean
    @ConditionalOnMissingBean(OAuth2ExceptionRenderer.class)
    public OAuth2ExceptionRenderer oAuth2ExceptionRenderer() {
        return new OAuth2ExceptionRendererImpl();
    }

    /**
     * 设置异常渲染服务
     */
    @Bean
    @ConditionalOnMissingBean(OAuth2AuthenticationEntryPoint.class)
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        oAuth2AuthenticationEntryPoint.setExceptionRenderer(oAuth2ExceptionRenderer());
        return oAuth2AuthenticationEntryPoint;
    }

}
