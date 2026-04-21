package com.feilong.im.config.security;

import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationProvider;
import com.feilong.im.config.security.authentication.sysuser.SysUserAuthenticationProvider;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.filter.RequestContextLifecycleFilter;
import com.feilong.im.filter.TokenAuthenticationFilter;
import com.feilong.im.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


/**
 * Spring Security配置
 * @author cfl 2026/4/15
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF，因为不使用session
                .csrf(AbstractHttpConfigurer::disable)
                // 允许跨域请求
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    // 使用 allowedOriginPatterns（推荐开发环境）,这会匹配所有域名，同时满足 Spring 的安全校验。
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    // 明确指定前端地址，推荐生产环境
                    // corsConfiguration.setAllowedOrigins(List.of("http://localhost:3005"));

                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    // 允许携带 Cookie/Token
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 公开访问路径
                        .requestMatchers(securityProperties.getIgnoreUrls()).permitAll()
                        .requestMatchers(securityProperties.getUnsecuredUrls()).permitAll()
                        // 如果 URL 在白名单中（如 /im*/**），即使添加了 @PreAuthorize 也不会生效
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 需ADMIN角色
                        // 其他 URL 需认证
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // session设置为无状态
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 异常处理
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 自定义认证失败处理器
                        .authenticationEntryPoint(authenticationEntryPoint)
                        // 自定义访问被拒绝处理器
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 验证和解析过滤器
                .addFilterBefore(new TokenAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RequestContextLifecycleFilter(), TokenAuthenticationFilter.class)
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
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider,
                                                       ImUserAuthenticationProvider imUserAuthenticationProvider,
                                                       SysUserAuthenticationProvider sysUserAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider, imUserAuthenticationProvider, sysUserAuthenticationProvider);
    }


}
