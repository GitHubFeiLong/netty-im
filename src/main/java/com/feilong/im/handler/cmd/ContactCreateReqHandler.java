package com.feilong.im.handler.cmd;

import com.feilong.im.config.NettyServerHandler;
import com.feilong.im.entity.ImFriend;
import com.feilong.im.enums.MessageErrorEnum;
import com.feilong.im.enums.cmd.MessageCmdContactEnum;
import com.feilong.im.exception.NettyClientException;
import com.feilong.im.handler.cmd.req.ContactCreateReq;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.service.ImFriendService;
import com.feilong.im.service.ImUserManageService;
import com.feilong.im.service.ImUserService;
import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 添加联系人
 * @see MessageCmdContactEnum#CREATE_REQ
 * @author cfl 2026/03/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContactCreateReqHandler implements CmdHandler {

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
        Long imUserId = NettyServerHandler.getCurrentUserId(ctx);
        ContactCreateReq req = JsonUtil.toObject(messageReq.getData(), ContactCreateReq.class);
        AssertUtil.isNotEquals(imUserId, req.getFriendId(), () -> new NettyClientException("不能添加自己为好友", MessageErrorEnum.CLIENT_PARAM_ERROR));
        log.info("用户{}添加好友{}", imUserId, req.getFriendId());
        // 校验是否已添加
        ImFriend imFriend = imFriendService.lambdaQuery().eq(ImFriend::getUserId, imUserId).eq(ImFriend::getFriendId, req.getFriendId()).one();
        if (imFriend != null) {
            log.info("用户{}已添加好友{}", imUserId, req.getFriendId());
            return;
        }

        log.info("用户{}开始添加好友{}", imUserId, req.getFriendId());
        // 需要添加两条记录
        ImFriend imFriend1 = new ImFriend();
        imFriend1.setUserId(imUserId);
        imFriend1.setFriendId(req.getFriendId());
        imFriendService.save(imFriend1);

        log.info("开始反向添加好友");
        ImFriend imFriend2 = new ImFriend();
        imFriend2.setUserId(req.getFriendId());
        imFriend2.setFriendId(imUserId);
        imFriendService.save(imFriend2);

        MessageResp resp = new MessageResp<>(messageReq, MessageCmdContactEnum.CREATE_RESP, null);

        ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(resp)));
    }

}
