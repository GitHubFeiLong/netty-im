package com.feilong.im.handler.cmd;

import com.feilong.im.config.NettyServerHandler;
import com.feilong.im.enums.cmd.MessageCmdSystemEnum;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 心跳请求处理
 * @author cfl 2026/03/31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemHeartbeatReqHandler implements CmdHandler {

    /**
     * 处理子命令
     *
     * @param ctx        上下文
     * @param messageReq 请求参数
     */
    @Override
    public void handle(ChannelHandlerContext ctx, MessageReq messageReq) {
        Channel currentChannel = ctx.channel();
        if (NettyServerHandler.channelUserMap.containsKey(currentChannel)) {
            MessageResp<String> messageResp = new MessageResp<>(messageReq, MessageCmdSystemEnum.HEARTBEAT_RESP, "pong");

            Long userId = NettyServerHandler.channelUserMap.get(currentChannel);
            log.debug("回复心跳: {} - {}", userId, currentChannel.remoteAddress());
            currentChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(messageResp)));
        } else {
            log.warn("用户状态异常，关闭连接");
            ctx.close();
        }
    }
}
