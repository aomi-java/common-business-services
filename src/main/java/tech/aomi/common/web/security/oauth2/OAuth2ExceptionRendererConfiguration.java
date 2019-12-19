package tech.aomi.common.web.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;

/**
 * OAuth2 错误结果渲染处理，返回JSON格式数据
 *
 * @author 田尘殇Sean(sean.snow @ live.com) createAt 2018/7/19
 */
@Configuration
@ConditionalOnClass(OAuth2AuthenticationEntryPoint.class)
public class OAuth2ExceptionRendererConfiguration {

    /**
     * OAuth2Exception异常处理服务
     */
    @Bean
    @ConditionalOnMissingBean(OAuth2ExceptionRenderer.class)
    public OAuth2ExceptionRenderer oAuth2ExceptionRenderer() {
        return new OAuth2ExceptionRendererImpl();
    }

    @Configuration
    public static class OAuth2ExceptionRendererConfigurerAdapter extends ResourceServerConfigurerAdapter {

        @Autowired(required = false)
        private OAuth2ExceptionRenderer oAuth2ExceptionRenderer;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            if (null != oAuth2ExceptionRenderer)
                resources.authenticationEntryPoint(oAuth2AuthenticationEntryPoint());
        }

        /**
         * 设置异常渲染服务
         */
        private OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
            OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
            oAuth2AuthenticationEntryPoint.setExceptionRenderer(oAuth2ExceptionRenderer);
            return oAuth2AuthenticationEntryPoint;
        }
    }
}
