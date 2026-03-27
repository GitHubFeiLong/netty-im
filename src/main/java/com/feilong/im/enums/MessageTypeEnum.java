package com.feilong.im.enums;

import lombok.Getter;

import java.util.List;

/**
 * @author cfl 2026/03/27
 */
@Getter
public enum MessageTypeEnum {
    /**
     * 系统级消息
     */
    SYSTEM("系统级消息", MessageCmdSystemEnum.class),
    /**
     * 会话相关操作
     */
    CONV,
    /**
     * 消息相关操作
     */
    MESSAGE,
    /**
     * 联系人操作
     */
    CONTACT,
    /**
     * 系统通知
     */
    NOTIFICATION,

    ;

    /**
     * 消息类型中文名称
     */
    private final String name;
    /**
     * 消息类型的子命令集
     */
    private final List<IMessageCmdEnum> cmds;

    MessageTypeEnum(String name, Class<? extends IMessageCmdEnum> clazz) {
        this.name = name;
        if (clazz != null && clazz.isEnum()) {
            IMessageCmdEnum[] enumConstants = clazz.getEnumConstants();
            this.cmds = enumConstants != null ? List.of(enumConstants) : List.of();
        } else {
            this.cmds = List.of();
        }
    }

    MessageTypeEnum(String name, List<IMessageCmdEnum> cmds) {
        this.name = name;
        this.cmds = cmds;
    }
}
