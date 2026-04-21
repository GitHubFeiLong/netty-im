package com.feilong.im.enums.cmd;

import com.feilong.im.handler.netty.cmd.CmdHandler;
import com.feilong.im.handler.netty.cmd.ContactCreateReqHandler;
import com.feilong.im.handler.netty.cmd.ContactPageReqHandler;
import com.feilong.im.handler.netty.cmd.NoneHandler;
import com.feilong.im.message.MessageReq;
import com.feilong.im.util.SpringBeanUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

/**
 * 联系人类型消息的子命令枚举
 * @author cfl 2026/03/27
 */
@Getter
public enum MessageCmdContactEnum implements IMessageCmdEnum {
    /**
     * 拉取联系人列表请求
     */
    PAGE_REQ("拉取联系人列表请求", SpringBeanUtil.getBean(ContactPageReqHandler.class)),
    /**
     * 拉取联系人列表响应
     */
    PAGE_RESP("拉取联系人列表响应"),
    /**
     * 创建联系人请求
     */
    CREATE_REQ("创建联系人请求", SpringBeanUtil.getBean(ContactCreateReqHandler.class)),
    /**
     * 创建联系人响应
     */
    CREATE_RESP("创建联系人响应"),
    /**
     * 删除联系人请求
     */
    DELETE_REQ("删除联系人请求"),
    /**
     * 删除联系人响应
     */
    DELETE_RESP("删除联系人响应"),
    /**
     * 更新联系人请求
     */
    UPDATE_REQ("更新联系人请求"),
    ;

    /**
     * 子命令名称
     */
    private final String name;

    /**
     * 子命令处理对象
     */
    private final CmdHandler cmdHandler;

    MessageCmdContactEnum(String name) {
        this.name = name;
        this.cmdHandler = SpringBeanUtil.getBean(NoneHandler.class);
    }

    MessageCmdContactEnum(String name, CmdHandler cmdHandler) {
        this.name = name;
        this.cmdHandler = cmdHandler;
    }

    /**
     * 处理子命令
     *
     * @param ctx 上下文
     * @param req 请求参数
     */
    @Override
    public void handler(ChannelHandlerContext ctx, MessageReq req) {
        cmdHandler.handle(ctx, req);
    }
}
