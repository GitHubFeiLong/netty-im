package com.feilong.im.handler.netty.cmd;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.feilong.im.handler.netty.NettyServerHandler;
import com.feilong.im.config.security.authentication.imuser.ImUserAuthenticationToken;
import com.feilong.im.config.security.authentication.imuser.ImUserDetails;
import com.feilong.im.config.security.token.TokenManager;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.MessageCmdSystemEnum;
import com.feilong.im.enums.status.ImUserStatusEnum;
import com.feilong.im.handler.netty.cmd.req.SystemAuthReq;
import com.feilong.im.handler.netty.cmd.resp.SystemAuthResp;
import com.feilong.im.handler.netty.cmd.resp.SystemOfflineResp;
import com.feilong.im.handler.netty.cmd.resp.SystemOnlineResp;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.service.ImUserManageService;
import com.feilong.im.service.ImUserService;
import com.feilong.im.util.AssertUtil;
import com.feilong.im.util.JsonUtil;
import com.feilong.im.util.ValidationUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证请求处理
 * @see MessageCmdSystemEnum#AUTH_REQ
 * @author cfl 2026/03/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemAuthReqHandler implements CmdHandler {

    private final ImUserService imUserService;
    private final ImUserManageService imUserManageService;
    private final TokenManager tokenManager;

    /**
     * 处理子命令
     *
     * @param ctx 上下文
     * @param messageReq 请求参数
     */
    @Override
    public void handle(ChannelHandlerContext ctx, MessageReq messageReq) {
        SystemAuthReq req = JsonUtil.toObject(messageReq.getData(), SystemAuthReq.class);
        ValidationUtil.validate(req);

        Long imUserId = null;
        String token = req.getToken();
        tokenManager.validateToken(token);
        Authentication authentication = tokenManager.parseToken(token);
        if (authentication instanceof ImUserAuthenticationToken imUserAuthenticationToken) {
            if (imUserAuthenticationToken.getPrincipal() instanceof ImUserDetails imUserDetails) {
                imUserId = imUserDetails.getId();
            }
        }

        AssertUtil.isNotNull(imUserId, "Token无效");

        log.info("用户{}进行登录IM", imUserId);
        // 获取或创建用户
        ImUserDTO catchById = imUserService.getCatchById(imUserId);
        if (catchById == null) {
            log.error("IM用户{}不存在", imUserId);
            throw new IllegalArgumentException("IM用户" + imUserId + "不存在");
        }

        // 如果该用户已存在其他连接，关闭旧连接
        Channel oldChannel = NettyServerHandler.userChannelMap.get(imUserId);
        Channel currentChannel = ctx.channel();
        if (oldChannel != null && oldChannel != currentChannel) {
            // 给旧连接发送离线通知
            SystemOfflineResp offlineResp = new SystemOfflineResp();
            offlineResp.setImUserId(imUserId);

            MessageResp<SystemOfflineResp> messageResp = new MessageResp<>(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.OFFLINE_RESP, offlineResp);
            oldChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(messageResp)));

            oldChannel.close();
        }

        // 保存用户与Channel的映射
        NettyServerHandler.userChannelMap.put(imUserId, currentChannel);
        NettyServerHandler.channelUserMap.put(currentChannel, imUserId);

        SystemAuthResp systemAuthResp = new SystemAuthResp();
        systemAuthResp.setImUserId(imUserId);
        MessageResp<SystemAuthResp> messageResp = new MessageResp<>(messageReq, MessageCmdSystemEnum.AUTH_RESP, systemAuthResp);
        // 回复登录信息
        currentChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(messageResp)));

        // 修改用户状态
        imUserService.updateStatus(imUserId, ImUserStatusEnum.ONLINE.getId());

        // 通知用户好友上线
        onlineNotifyFriends(imUserId, currentChannel);

        log.info("用户 {} 上线，当前在线人数: {}", imUserId, NettyServerHandler.userChannelMap.size());
    }

    /**
     * 通知用户好友上线
     * @param imUserId  用户ID
     * @param currentChannel 用户ID对应的channel
     */
    private void onlineNotifyFriends(Long imUserId, Channel currentChannel) {
        log.info("查询与用户进行单聊其他用户，通知用户上线");
        List<Long> friendIds = imUserManageService.listFriendIds(imUserId);
        if (CollectionUtils.isNotEmpty(friendIds)) {
            // 广播上线通知
            SystemOnlineResp systemOnlineResp = new SystemOnlineResp();
            systemOnlineResp.setImUserId(imUserId);
            MessageResp<SystemOnlineResp> onlineMessage = new MessageResp<>(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.ONLINE_RESP, systemOnlineResp);

            for (Long otherUserId : friendIds) {
                Channel otherUserChannel = NettyServerHandler.userChannelMap.get(otherUserId);
                if (otherUserChannel != null && otherUserChannel != currentChannel && otherUserChannel.isActive()) {
                    otherUserChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(onlineMessage)));
                }
            }
        }
    }
}
