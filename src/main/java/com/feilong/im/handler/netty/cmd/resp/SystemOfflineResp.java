package com.feilong.im.handler.netty.cmd.resp;

import lombok.Data;

/**
 * 登出响应
 * @author cfl 2026/03/30
 */
@Data
public class SystemOfflineResp {
    /**
     * 用户id
     */
    private Long imUserId;
}
