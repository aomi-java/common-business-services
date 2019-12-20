package tech.aomi.common.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 基本web安全配置
 * 项目中继承该类，重写{@link WebSecurityConfigurerAdapter#configure(HttpSecurity)} 时，调用{@code super.configure(http)}
 * 使得配置生效
 *
 * @author Sean Create At 2019/12/20
 */
public abstract class AbstractWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired(required = false)
    private AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    @Autowired(required = false)
    private Http403ForbiddenImpl http403ForbiddenImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (null != accessDeniedHandlerImpl) {
            http.exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl);
        }
        if (null != http403ForbiddenImpl) {
            http.exceptionHandling().authenticationEntryPoint(http403ForbiddenImpl);
        }
    }
}
