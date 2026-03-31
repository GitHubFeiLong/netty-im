package com.feilong.im.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * im会话表
 * @author cfl 2026/3/26
 */
@Data
public class ImConvDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    private Long id;

    /**
     * 会话类型（1-单聊，2-群聊）
     */
    private Integer convType;

    /**
     * 单聊时：用户1 ID（与user2_id组合唯一，需满足user1_id < user2_id）
     */
    private Long user1Id;

    /**
     * 单聊时：用户2 ID（单聊必填，群聊为空）
     */
    private Long user2Id;

    /**
     * 群聊时：关联group_id（群聊必填，单聊为空）
     */
    private Long groupId;

    /**
     * 最后一条消息ID
     */
    private Long lastMsgId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户1信息
     */
    private ImUserDTO user1;

    /**
     * 用户2信息
     */
    private ImUserDTO user2;

    /**
     * 最后一条消息
     */
    private ImMessageDTO lastMsg;

    /**
     * 未读消息数
     */
    private Integer unreadCount;
}
