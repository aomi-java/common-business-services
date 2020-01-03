package tech.aomi.common.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 根据用户ID查找用户
 *
 * @author Sean Create At 2020/1/3
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    /**
     * 加载用户信息通过ID
     *
     * @param id 用户ID
     * @return 用户信息
     * @throws UsernameNotFoundException
     */
    default UserDetails loadUserById(String id) throws UsernameNotFoundException {
        return null;
    }

}
