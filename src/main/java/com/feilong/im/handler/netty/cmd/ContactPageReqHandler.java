package com.feilong.im.handler.netty.cmd;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feilong.im.handler.netty.NettyServerHandler;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.enums.cmd.MessageCmdContactEnum;
import com.feilong.im.handler.netty.cmd.req.ContactPageReq;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.service.ImFriendService;
import com.feilong.im.service.ImUserManageService;
import com.feilong.im.service.ImUserService;
import com.feilong.im.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 拉取联系人
 * @see MessageCmdContactEnum#PAGE_REQ
 * @author cfl 2026/03/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContactPageReqHandler implements CmdHandler {

    private final ImUserService imUserService;
    private final ImFriendService imFriendService;
    private final ImUserManageService imUserManageService;

    /**
     * 处理子命令
     *
     * @param ctx 上下文
     * @param messageReq 请求参数
     */
    @Override
    public void handle(ChannelHandlerContext ctx, MessageReq messageReq) {
        ContactPageReq req = JsonUtil.toObject(messageReq.getData(), ContactPageReq.class);

        Long imUserId = NettyServerHandler.getCurrentUserId(ctx);;

        log.info("查询用户{}的好友列表:{}", imUserId, messageReq);

        Page<ImUserDTO> imUserPage = imFriendService.listPage(imUserId, req);

        MessageResp<Page<ImUserDTO>> messageResp = new MessageResp<>(messageReq, MessageCmdContactEnum.PAGE_RESP, imUserPage);

        ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(messageResp)));
    }

}
