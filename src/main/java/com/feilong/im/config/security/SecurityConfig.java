package com.feilong.im.config.security;

import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationProvider;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.filter.TokenAuthenticationFilter;
import com.feilong.im.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security配置
 * @author cfl
 * @version 1.0.0
 * @date 2025-04-03
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final TokenManager tokenManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //~methods
    //==================================================================================================================


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF，因为不使用session
                .csrf(AbstractHttpConfigurer::disable)
                //允许跨域请求
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 公开访问路径
                        .requestMatchers(securityProperties.getIgnoreUrls()).permitAll()
                        .requestMatchers(securityProperties.getUnsecuredUrls()).permitAll()
                        // 放行 actuator 端点
                        .requestMatchers("/actuator/**").permitAll()
                        // 放行swagger, knife4j 相关路径
                        .requestMatchers("/doc.html", "/webjars/**", "/favicon.ico", "/v3/api-docs/swagger-config", "/v3/api-docs").permitAll()
                        // .requestMatchers("/admin/**").hasRole("ADMIN")  // 需ADMIN角色
                        // .requestMatchers("/api/**")
                        // .access((authentication, object) -> accessDecisionManager.decode(authentication, object))
                        // .access((authentication, object) -> {
                        //     // 自定义授权逻辑
                        //     if (authentication != null && authentication.get() != null && authentication.get().isAuthenticated()) {
                        //         // 检查用户是否有特定权限
                        //         Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
                        //         if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                        //             return new AuthorizationDecision(true); // 允许访问
                        //         }
                        //     }
                        //     return new AuthorizationDecision(false); // 拒绝访问
                        // })
                        .anyRequest()
                        // .access(authorizationManager)
                        // .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        // session设置为无状态
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // .addFilterBefore(filterSecurityInterceptor(), AuthorizationFilter.class)
                // 异常处理
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 自定义认证失败处理器
                        .authenticationEntryPoint(authenticationEntryPoint)
                        // 自定义访问被拒绝处理器
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 验证和解析过滤器
                .addFilterBefore(new TokenAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class)

        ;
        return http.build();
    }

    /**
     * 账号状态检查
     * @return
     */
    @Bean
    public UserDetailsChecker userDetailsChecker() {
        return new AccountStatusUserDetailsChecker();
    }

    /**
     * 默认密码认证的 Provider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }


    /**
     * 配置认证管理器，按照顺序进行认证
     * <p></p>
     * @param daoAuthenticationProvider 密码认证
     * @param imUserAuthenticationProvider imUser认证
     * @return AuthenticationManager 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider, ImUserAuthenticationProvider imUserAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider, imUserAuthenticationProvider);
    }


}
