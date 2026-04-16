package com.feilong.im.dto.form;

import java.time.LocalDateTime;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * im会话表 Save Form
 * @see com.feilong.im.entity.ImConv
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@Schema(description = "ImConvSaveForm")
public class ImConvSaveForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话类型（1-单聊，2-群聊）
     */
    @Schema(description = "会话类型（1-单聊，2-群聊）")
    private Byte type;

    /**
     * 单聊时：用户1 ID（与user2_id组合唯一，需满足user1_id < user2_id）
     */
    @Schema(description = "单聊时：用户1 ID（与user2_id组合唯一，需满足user1_id < user2_id）")
    private Long user1Id;

    /**
     * 单聊时：用户2 ID（单聊必填，群聊为空）
     */
    @Schema(description = "单聊时：用户2 ID（单聊必填，群聊为空）")
    private Long user2Id;

    /**
     * 群聊时：关联group_id（群聊必填，单聊为空）
     */
    @Schema(description = "群聊时：关联group_id（群聊必填，单聊为空）")
    private Long groupId;
}
