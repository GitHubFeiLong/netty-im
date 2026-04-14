package com.feilong.im.context;

import lombok.Data;

/**
 * 当前用户信息
 * @author cfl 2026/04/08
 */
@Data
public class CurrentUser {
    /**
     * JWT ID
     */
    private String jwtId;

    /**
     * 用户ID
     */
    private Long userId;
}
