package com.feilong.im.enums;

import com.feilong.im.enums.cmd.*;
import com.google.common.base.Strings;
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
    CONV("会话消息", MessageCmdConvEnum.class),
    /**
     * 消息相关操作
     */
    MESSAGE("消息相关操作", MessageCmdMessageEnum.class),
    /**
     * 联系人操作
     */
    CONTACT("联系人操作", MessageCmdContactEnum.class),
    /**
     * 系统通知
     */
    NOTIFICATION("系统通知", MessageCmdNotificationEnum.class),

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

    /**
     * 根据子命令名称获取枚举
     * @param cmdName 子命令名称
     * @return 子命令枚举，找不到返回 null
     */
    public IMessageCmdEnum getCmdEnumByName(String cmdName) {
        if (Strings.isNullOrEmpty(cmdName)) {
            return null;
        }
        return cmds.stream()
                .filter(cmd -> cmd.name().equals(cmdName))
                .findFirst()
                .orElse(null);
    }
}
