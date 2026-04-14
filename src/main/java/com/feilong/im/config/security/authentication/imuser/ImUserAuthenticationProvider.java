package com.feilong.im.config.security.authentication.imuser;

import com.feilong.im.entity.ImUser;
import com.feilong.im.service.ImUserService;
import com.feilong.im.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * IM用户登录
 * @author cfl 2026/04/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImUserAuthenticationProvider implements AuthenticationProvider {

    private final ImUserService imUserService;
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
        if (authentication instanceof ImUserAuthenticationToken token) {
            ImUser imUser = imUserService.lambdaQuery().eq(ImUser::getUsername, token.getPrincipal()).one();

            AssertUtil.isNotNull(imUser, () -> new UsernameNotFoundException("用户不存在"));
            // 比较密码
            AssertUtil.isTrue(passwordEncoder.matches((String) token.getCredentials(), imUser.getPassword()), () -> new BadCredentialsException("密码错误"));

            // 构建用户信息
            // 已认证
            ImUserDetails imUserDetails = new ImUserDetails();
            imUserDetails.setId(imUser.getId());
            imUserDetails.setUsername(imUser.getUsername());
            imUserDetails.setNickname(imUser.getNickname());

            return new ImUserAuthenticationToken(imUserDetails, null);
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
        return ImUserAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
