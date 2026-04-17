package com.feilong.im.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户好友表 Save Form
 * @see com.feilong.im.entity.ImFriend
 * @author cfl 2026/04/16
 */
@Data
@Accessors(chain = true)
@Schema(description = "ImFriendSaveForm")
public class ImFriendSaveForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 好友ID
     */
    @Schema(description = "好友ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long friendId;
}
