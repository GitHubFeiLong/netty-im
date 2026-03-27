package com.feilong.im.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * im用户会话表
 * @author cfl 2026/3/26
 */
@Data
public class ImConvUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 会话ID
     */
    private Long convId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 该用户在此会话的未读消息数（初始0，新消息+1，阅读后清零）
     */
    private Integer unreadCount;

    /**
     * 该用户最后阅读的消息ID（用于定位未读起点）
     */
    private Long lastReadId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
}
