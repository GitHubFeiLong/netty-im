package com.feilong.im.handler.netty.cmd;

import com.feilong.im.message.MessageReq;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cfl 2026/03/31
 */
@Slf4j
@Component
public class NoneHandler implements CmdHandler{

    /**
     * 处理子命令
     *
     * @param ctx        上下文
     * @param messageReq 请求参数
     */
    @Override
    public void handle(ChannelHandlerContext ctx, MessageReq messageReq) {
        log.info("无处理子命令");
    }
}
