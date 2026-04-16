package com.feilong.im.config.security.authentication.sysuser;

import com.feilong.im.entity.SysUser;
import com.feilong.im.service.SysUserService;
import com.feilong.im.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 系统用户认证供应者
 * @author cfl 2026/04/16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SysUserAuthenticationProvider implements AuthenticationProvider {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 比较是否匹配
        if (authentication instanceof SysUserAuthenticationToken token) {
            SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, token.getPrincipal()).one();

            AssertUtil.isNotNull(sysUser, () -> new UsernameNotFoundException("用户不存在"));
            // 比较密码
            AssertUtil.isTrue(passwordEncoder.matches((String) token.getCredentials(), sysUser.getPassword()), () -> new BadCredentialsException("密码错误"));

            // 构建用户信息
            // 已认证
            SysUserDetails userDetails = new SysUserDetails();
            userDetails.setId(sysUser.getId());
            userDetails.setUsername(sysUser.getUsername());
            userDetails.setRoles(Arrays.stream(sysUser.getRoles().split(",")).toList());
            List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream().map(SimpleGrantedAuthority::new).toList();

            return new SysUserAuthenticationToken(userDetails, authorities);
        }
        throw new RuntimeException("认证方式错误");
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * <code>Authentication</code> object. It simply indicates it can support closer
     * evaluation of it. An <code>AuthenticationProvider</code> can still return
     * <code>null</code> from the {@link #authenticate(Authentication)} method to indicate
     * another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SysUserAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
