package com.feilong.im.handler.cmd;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.feilong.im.config.NettyServerHandler;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.dto.ImMessageDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImMessage;
import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.MessageCmdConvEnum;
import com.feilong.im.handler.cmd.req.ConvCreateReq;
import com.feilong.im.handler.cmd.req.SystemAuthReq;
import com.feilong.im.handler.cmd.resp.ConvCreateResp;
import com.feilong.im.mapstruct.ImMessageEntityMapper;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.service.*;
import com.feilong.im.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cfl 2026/03/31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConvCreateReqHandler implements CmdHandler{

    private final ImConvManageService imConvManageService;
    private final ImUserService imUserService;
    private final ImMessageService imMessageService;
    private final ImMessageEntityMapper imMessageEntityMapper;

    /**
     * 处理子命令
     *
     * @param ctx        上下文
     * @param messageReq 请求参数
     */
    @Override
    public void handle(ChannelHandlerContext ctx, MessageReq messageReq) {
        log.info("处理创建会话");
        ConvCreateReq req = JsonUtil.toObject(messageReq.getData(), ConvCreateReq.class);
        Long currentUserId = NettyServerHandler.getCurrentUserId(ctx);
        Long userId = req.getUserId();

        // 参数断言
        Assert.isTrue(currentUserId != null,  "用户1ID不能为空");
        Assert.isTrue(userId != null,  "用户2ID不能为空");
        Assert.isFalse(currentUserId.equals(userId),  "不能自己跟自己创建会话");

        long minUserId;
        long maxUserId;
        if (currentUserId < userId) {
            minUserId = currentUserId;
            maxUserId = userId;
        } else {
            minUserId = userId;
            maxUserId = currentUserId;
        }
        // 创建并获取单聊会话
        ImConvDTO singleConv = imConvManageService.createSingleConv(minUserId, maxUserId);

        // 查询两个用户信息
        ImUserDTO currentUser = imUserService.getCatchById(currentUserId);
        ImUserDTO friendUser = imUserService.getCatchById(userId);
        singleConv.setUser1Id(currentUserId);
        singleConv.setUser2Id(userId);
        singleConv.setUser1(currentUser);
        singleConv.setUser2(friendUser);
        ConvCreateResp resp = new ConvCreateResp();
        resp.setConv(singleConv);

        // 查询最新一条消息
        if (singleConv.getLastMsgId() != null) {
            ImMessage imMessage = imMessageService.getById(singleConv.getLastMsgId());
            ImMessageDTO lastMsg = imMessageEntityMapper.toDto(imMessage);
            singleConv.setLastMsg(lastMsg);
        }

        // 发送
        MessageResp<ConvCreateResp> messageResp = new MessageResp<>(messageReq, MessageCmdConvEnum.CREATE_RESP, resp);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(messageResp)));

        // 对方push一条新会话


    }
}
