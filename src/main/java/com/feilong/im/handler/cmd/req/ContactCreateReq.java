package com.feilong.im.handler.cmd.req;

import lombok.Data;

/**
 * 创建联系人请求
 * @author cfl 2026/04/07
 */
@Data
public class ContactCreateReq {
    /**
     * 用户ID
     */
    private Long friendId;
}
