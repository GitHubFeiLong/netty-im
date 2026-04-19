package com.feilong.im.util;

import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.config.security.authentication.sysuser.SysUserDetails;
import com.feilong.im.exception.ClientException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 当前用户工具类
 * 从Spring Security 上下文中获取当前用户信息
 * @author cfl 2026/04/16
 */
public class CurrentUserUtil {


    /**
     * 获取当前认证对象
     *
     * @return Authentication 对象
     * @throws InsufficientAuthenticationException 如果未认证
     */
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("用户未认证");
        }
        return authentication;
    }

    /**
     * 获取当前 IM 用户详情
     *
     * @return ImUserDetails
     * @throws ClientException 如果不是 IM 用户或未认证
     * @throws InsufficientAuthenticationException 如果未认证
     */
    public static ImUserDetails getCurrentImUser() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof ImUserDetails)) {
            throw ClientException.of("当前用户不是 IM 用户");
        }

        return (ImUserDetails) principal;
    }

    /**
     * 获取当前系统用户详情
     *
     * @return SysUserDetails
     * @throws ClientException 用户不是系统用户
     * @throws InsufficientAuthenticationException 如果未认证
     */
    public static SysUserDetails getCurrentSysUser() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof SysUserDetails)) {
            throw ClientException.of("当前用户不是系统用户");
        }

        return (SysUserDetails) principal;
    }

    /**
     * 获取当前用户 ID
     * 支持 IM 用户和系统用户
     *
     * @return 用户 ID
     * @throws InsufficientAuthenticationException 如果未认证
     */
    public static Long getCurrentUserId() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof ImUserDetails imUserDetails) {
            return imUserDetails.getId();
        } else if (principal instanceof SysUserDetails sysUserDetails) {
            return sysUserDetails.getId();
        }

        throw new InsufficientAuthenticationException("无法获取用户ID");
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     * @throws InsufficientAuthenticationException 如果未认证
     */
    public static String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof ImUserDetails imUserDetails) {
            return imUserDetails.getUsername();
        } else if (principal instanceof SysUserDetails sysUserDetails) {
            return sysUserDetails.getUsername();
        }

        throw new InsufficientAuthenticationException("无法获取用户名");
    }

    /**
     * 获取当前用户的 Token ID
     *
     * @return Token ID
     * @throws InsufficientAuthenticationException 如果未认证
     */
    public static String getCurrentTokenId() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof ImUserDetails imUserDetails) {
            return imUserDetails.getTokenId();
        } else if (principal instanceof SysUserDetails sysUserDetails) {
            return sysUserDetails.getTokenId();
        }

        throw new InsufficientAuthenticationException("无法获取Token ID");
    }

    /**
     * 检查当前用户是否已认证
     *
     * @return true-已认证，false-未认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
