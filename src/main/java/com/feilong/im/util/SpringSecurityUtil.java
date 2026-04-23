package com.feilong.im.util;

import com.feilong.im.constant.SecurityConstants;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security工具类
 * @author cfl 2026/04/20
 */
public class SpringSecurityUtil {

    /**
     * 判断当前用户是否有指定角色
     * @param role 角色名（不需要 ROLE_ 前缀）
     * @return true-有该角色，false-没有
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String roleWithPrefix = SecurityConstants.ROLE_PREFIX + role.toUpperCase();
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(roleWithPrefix));
    }

    /**
     * 判断当前用户是否有指定权限
     * @param authority 权限标识
     * @return true-有该权限，false-没有
     */
    public static boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals( authority));
    }

    /**
     * 判断当前用户是否是管理员
     * @return true-是管理员，false-不是
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }
}
