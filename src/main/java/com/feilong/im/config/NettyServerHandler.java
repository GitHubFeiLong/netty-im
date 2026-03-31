package com.feilong.im.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.feilong.im.enums.MessageTypeEnum;
import com.feilong.im.enums.cmd.MessageCmdSystemEnum;
import com.feilong.im.handler.cmd.SystemHeartbeatReqHandler;
import com.feilong.im.handler.cmd.resp.SystemOfflineResp;
import com.feilong.im.message.MessageReq;
import com.feilong.im.message.MessageResp;
import com.feilong.im.service.*;
import com.feilong.im.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cfl 2026/02/26
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 存储所有连接的客户端
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存储用户ID和Channel的对应关系
     */
    public static ConcurrentHashMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();

    /**
     * 存储Channel和用户ID的对应关系
     */
    public static ConcurrentHashMap<Channel, Long> channelUserMap = new ConcurrentHashMap<>();

    /**
     * 获取当前用户ID
     * @param ctx 上下文
     * @return 用户ID
     */
    public static Long getCurrentUserId(ChannelHandlerContext ctx) {
        return channelUserMap.get(ctx.channel());
    }


    private final ImUserService imUserService;
    private final ImUserManageService imUserManageService;
    private final ImConvManageService imConvManageService;
    private final ImMessageManageService imMessageManageService;
    private final ImConvService imConvService;
    private final ThreadService threadService;


    /**
     * 读取客户端消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        log.info("收到客户端消息: {}", text);
        try {
            threadService.execute(ctx, () -> {
                MessageReq messageReq = JsonUtil.toObject(text, MessageReq.class);
                // 检查参数
                messageReq.check();
                // 处理消息
                messageReq.getCmdEnum().handler(ctx, messageReq);
            });

            // switch (chatMessage.getType()) {
            //     case HEARTBEAT:
            //         handleHeartbeat(ctx);
            //         break;
            //     case ONLINE:
            //         handleOnline(ctx, chatMessage);
            //         break;
            //     case CHAT:
            //         handleChatMessage(ctx, chatMessage);
            //         break;
            //     case CREATE_SINGLE_CONVERSATION:
            //         handleCreateSingleConversation(ctx, chatMessage);
            //         break;
            //     case PULL_CONVERSATION_LIST:
            //         handlePullConversationList(ctx, chatMessage);
            //         break;
            //     case PULL_CONVERSATION_DETAIL:
            //         handlePullConversationDetail(ctx, chatMessage);
            //         break;
            //     case PULL_MESSAGE_LIST:
            //         handlePullMessageList(ctx, chatMessage);
            //         break;
            //     case CONVERSATION_MESSAGE_READ_MARK:
            //         handleConversationMessageReadMark(ctx, chatMessage);
            //         break;
            //     case DELETE_CONVERSATION:
            //         handleDeleteConversation(ctx, chatMessage);
            //         break;
            //     default:
            //         log.warn("未知的消息类型: {}", chatMessage.getType());
            // }
        } catch (Exception e) {
            log.error("消息解析失败", e);
            throw e;
        }
    }

    /**
     * 客户端连接成功
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channels.add(channel);
        log.info("客户端连接: {}", channel.remoteAddress());
    }

    /**
     * 当处理器从 Pipeline 移除时触发，连接关闭前
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        threadService.execute(ctx, () -> {
            Channel channel = ctx.channel();
            log.info("客户端断开连接: {}", channel.remoteAddress());
            channels.remove(channel);

            Long userId = channelUserMap.remove(channel);
            if (userId != null) {
                // 用户ID 不同的Channel
                if (userChannelMap.containsKey(userId)) {
                    // 判断是否是同一个channel，避免删错
                    if (Objects.equals(userChannelMap.get(userId),  channel)) {
                        userChannelMap.remove(userId);

                        // 修改用户状态
                        if (userId > 0) {
                            imUserService.updateStatus(userId, 0);
                        }

                        log.info("查询与用户进行单聊其他用户，通知用户下线");
                        // 查询该用户的会话列表的用户
                        List<Long> friendIds = imUserManageService.listFriendIds(userId);
                        if (CollectionUtils.isNotEmpty(friendIds)) {
                            // 广播上线通知
                            SystemOfflineResp offlineResp = new SystemOfflineResp();
                            offlineResp.setImUserId(userId);
                            MessageResp<SystemOfflineResp> systemOfflineResp = new MessageResp<>(MessageTypeEnum.SYSTEM, MessageCmdSystemEnum.OFFLINE_RESP, offlineResp);
                            for (Long friendId : friendIds) {
                                Channel friendChannel = userChannelMap.get(friendId);
                                if (friendChannel != null && friendChannel != channel && friendChannel.isActive()) {
                                    friendChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(systemOfflineResp)));
                                }
                            }
                        }
                    }

                    log.info("用户 {} 离线，当前在线人数: {}", userId, userChannelMap.size());
                }
            }

            log.info("客户端断开: {}", channel.remoteAddress());
        });
    }

    /**
     * 捕获处理过程中的异常 发生 I/O 或业务异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        NettyServerHandler.exceptionCaughtStatic(ctx, cause);
    }

    /**
     * 空闲检测
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.debug("userEventTriggered 进入了");
        if (evt instanceof IdleStateEvent) {
            log.debug("userEventTriggered 进入了 IdleStateEvent");
            IdleStateEvent event = (IdleStateEvent) evt;
            // 服务端检测到读空闲（客户端超过一段时间没发消息）
            if (event.state() == IdleState.READER_IDLE) {
                log.debug("又检测到读空闲了！");
                if (channelUserMap.containsKey(ctx.channel())) {
                    Long userId = channelUserMap.get(ctx.channel());
                    log.debug("读空闲，发送心跳: {} - {}", userId, ctx.channel().remoteAddress());
                    // 响应会话
                    MessageResp<String> messageResp = new MessageResp<>(
                            MessageTypeEnum.SYSTEM,
                            MessageCmdSystemEnum.HEARTBEAT_REQ,
                            "pong"
                    );
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(messageResp)));
                } else {
                    ctx.close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 处理心跳消息
     * @param ctx 上下文
     */
    private void handleHeartbeat(ChannelHandlerContext ctx) {
        threadService.execute(ctx, () -> {
            log.debug("收到客户端心跳请求");
            Channel currentChannel = ctx.channel();
            // 响应会话
            // sendSystemMessage(ChartMessageTypeEnum.HEARTBEAT, currentChannel, "pong");
        });
    }

    /**
     * 处理用户上线
     * @param ctx channel上下文
     * @param chatMessage       接收到客户端的消息
     */
    // private void handleOnline(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         Long userId = chatMessage.getFromUserId();
    //         log.info("用户{}进行登录IM", userId);
    //         // 获取或创建用户
    //         ImUser catchById = imUserService.getOrCreateById(userId);
    //         if (catchById == null) {
    //             log.error("IM用户{}不存在", userId);
    //             throw new IllegalArgumentException("IM用户" + userId + "不存在");
    //         }
    //
    //         // 如果该用户已存在其他连接，关闭旧连接
    //         Channel oldChannel = userChannelMap.get(userId);
    //         Channel currentChannel = ctx.channel();
    //         if (oldChannel != null && oldChannel != currentChannel) {
    //             // 给旧连接发送离线通知
    //             sendSystemMessage(ChartMessageTypeEnum.OFFLINE, oldChannel, "挤下线通知");
    //             oldChannel.close();
    //         }
    //
    //         // 保存用户与Channel的映射
    //         userChannelMap.put(userId, currentChannel);
    //         channelUserMap.put(currentChannel, userId);
    //
    //         // 回复登录信息
    //         sendSystemMessage(ChartMessageTypeEnum.ONLINE, currentChannel, ChatMessageServerContentUtil.genOnlineDTO(chatMessage));
    //         if (userId > 0) {
    //             // 修改用户状态
    //             imUserService.updateStatus(userId, ImUserStatusEnum.ONLINE.getId());
    //         }
    //
    //         log.info("查询与用户进行单聊其他用户，通知用户上线");
    //         // 查询该用户的会话列表的用户
    //         Set<Long> otherUserIds = imUserManageService.queryUserSingleSessionPartnerIds(userId);
    //         if (CollectionUtils.isNotEmpty(otherUserIds)) {
    //             // 广播上线通知
    //             ChatMessage<IServerContent> onlineMessage = new ChatMessage<IServerContent>(
    //                     CommonConst.SYSTEM_USER_ID,
    //                     CommonConst.ALL_USER_ID,
    //                     new BroadcastOnlineDTO(userId),
    //                     ChartMessageTypeEnum.CONV_USER_ONLINE
    //             );
    //             for (Long otherUserId : otherUserIds) {
    //                 Channel otherUserChannel = userChannelMap.get(otherUserId);
    //                 if (otherUserChannel != null && otherUserChannel != currentChannel && otherUserChannel.isActive()) {
    //                     otherUserChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(onlineMessage)));
    //                 }
    //             }
    //         }
    //
    //         log.info("用户 {} 上线，当前在线人数: {}", userId, userChannelMap.size());
    //     });
    // }

    /**
     * 处理聊天消息
     * @param ctx   上下文
     * @param chatMessage   聊天消息
     */
    // private void handleChatMessage(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("收到用户{}发送的聊天信息", chatMessage.getFromUserId());
    //         Channel currentChannel = ctx.channel();
    //         Long convId = ChatMessageClientContentUtil.getConvId(chatMessage);
    //         // 持久化消息
    //         ImMessage imMessage = imMessageManageService.sendSingleMessage(convId, chatMessage);
    //         String clientMessageId = ChatMessageClientContentUtil.getMessageId(chatMessage);
    //         // 回复客户端已收到消息
    //         ChatMessage<IServerContent> message = new ChatMessage<IServerContent>(
    //                 CommonConst.SYSTEM_USER_ID,
    //                 channelUserMap.get(currentChannel),
    //                 new ChatMessageDTO(clientMessageId, imMessage.getId()),
    //                 ChartMessageTypeEnum.CHAT
    //         );
    //         // 回复消息
    //         sendMessageToUser(currentChannel, message);
    //
    //         log.info("转发消息到接收者{}", chatMessage.getToUserId());
    //         // 给接收者发送消息
    //         ImConvDTO imConv = imConvService.getCatchById(convId);
    //         if (Objects.equals(imConv.getConvType(), ImConvTypeEnum.SINGLE.getId())) {
    //             Long toUserId = chatMessage.getToUserId();
    //             Channel toChannel = userChannelMap.get(toUserId);
    //
    //             // 查询会话未读数量
    //             Integer unreadCount = imMessageManageService.getUnreadCount(convId, toUserId);
    //
    //             if (toChannel != null && toChannel.isActive()) {
    //                 log.info("接收者在线，转发消息给用户");
    //                 ServerForwardChat2ReceiverDTO serverForwardChat2ReceiverDTO = new ServerForwardChat2ReceiverDTO(
    //                         convId,
    //                         imMessage.getId(),
    //                         JsonUtil.toJsonString(chatMessage.getContent(), ClientContentConst.MESSAGE_ID),
    //                         unreadCount
    //                 );
    //                 // 客户端已收到消息
    //                 ChatMessage<IServerContent> message1 = new ChatMessage<IServerContent>(
    //                         channelUserMap.get(currentChannel),
    //                         toUserId,
    //                         serverForwardChat2ReceiverDTO,
    //                         ChartMessageTypeEnum.CHAT
    //                 );
    //                 sendMessageToUser(toChannel, message1);
    //                 log.info("消息已发送给用户: {}", toUserId);
    //             }
    //         }
    //     });
    // }

    /**
     * 处理创建单聊会话消息
     * @param ctx   上下文
     * @param chatMessage   客户端消息
     */
    // private void handleCreateSingleConversation(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("处理创建单聊会话消息");
    //         Channel currentChannel = ctx.channel();
    //         // 创建单聊会话
    //         ImConvDTO singleConv = imConvManageService.createSingleConv(channelUserMap.get(currentChannel), ChatMessageClientContentUtil.getToUserId(chatMessage));
    //         // 响应会话
    //         sendSystemMessage(ChartMessageTypeEnum.CREATE_SINGLE_CONVERSATION, currentChannel, ChatMessageServerContentUtil.genCreateSingleConversationDTO(chatMessage, singleConv.getId()));
    //     });
    // }

    /**
     * 处理拉取会话列表消息
     * @param ctx 上下文
     * @param chatMessage 客户端消息
     */
    // private void handlePullConversationList(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("处理拉取会话列表消息");
    //         Channel currentChannel = ctx.channel();
    //         // 查询所有会话
    //         List<ImConvDTO> imConvList = imConvManageService.listConvListByUserId(channelUserMap.get(currentChannel));
    //         // 响应会话
    //         sendSystemMessage(ChartMessageTypeEnum.PULL_CONVERSATION_LIST, currentChannel, ChatMessageServerContentUtil.genPullConversationListDTO(chatMessage, imConvList));
    //     });
    // }

    /**
     * 处理拉取会话详情消息
     * @param ctx 上下文
     * @param chatMessage 客户端消息
     */
    // private void handlePullConversationDetail(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("处理拉取会话详情");
    //         Long convId = ChatMessageClientContentUtil.getConvId(chatMessage);
    //         Channel currentChannel = ctx.channel();
    //         // 查询会话详情
    //         ImConvDTO convDetail = imConvManageService.getConvDetail(convId);
    //         if (convDetail == null) {
    //             throw new IllegalArgumentException("会话" + convId + "不存在");
    //         }
    //         // 修改下user1 和 user2 信息
    //         if (Objects.equals(convDetail.getConvType(), ImConvTypeEnum.SINGLE.getId())) {
    //             // user1Id是其他用户，就需要修改内容
    //             if (!Objects.equals(convDetail.getUser1Id(), channelUserMap.get(currentChannel))) {
    //                 Long user1Id = convDetail.getUser1Id();
    //                 Long user2Id = convDetail.getUser2Id();
    //                 ImUserDTO user1 = convDetail.getUser1();
    //                 ImUserDTO user2 = convDetail.getUser2();
    //
    //                 convDetail.setUser1Id(user2Id);
    //                 convDetail.setUser2Id(user1Id);
    //                 convDetail.setUser1(user2);
    //                 convDetail.setUser2(user1);
    //             }
    //         }
    //         // 响应会话
    //         sendSystemMessage(ChartMessageTypeEnum.PULL_CONVERSATION_DETAIL, currentChannel, new PullConversationDetailDTO(ChatMessageClientContentUtil.getMessageId(chatMessage), convDetail));
    //     });
    // }

    /**
     * 处理拉取消息列表消息
     * @param ctx 上下文
     * @param chatMessage 客户端消息
     */
    // private void handlePullMessageList(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("处理拉取消息列表消息");
    //         Channel currentChannel = ctx.channel();
    //         Long convId = ChatMessageClientContentUtil.getConvId(chatMessage);
    //         Long minMsgId = ChatMessageClientContentUtil.getMinMsgId(chatMessage);
    //         // 查询消息列表
    //         List<ImMessageDTO> imMessageDTOS = imMessageManageService.listHistoryMessage(convId, minMsgId, CommonConst.LIMIT_HISTORY_MESSAGE_COUNT);
    //         // 响应会话
    //         sendSystemMessage(ChartMessageTypeEnum.PULL_MESSAGE_LIST, currentChannel, ChatMessageServerContentUtil.genPullMessageListDTO(chatMessage, imMessageDTOS));
    //     });
    // }

    /**
     * 处理会话消息已读标记
     * @param ctx
     * @param chatMessage
     */
    // private void handleConversationMessageReadMark(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("处理会话消息已读标记");
    //         Channel currentChannel = ctx.channel();
    //         Long fromUserId = channelUserMap.get(currentChannel);
    //         Long convId = ChatMessageClientContentUtil.getConvId(chatMessage);
    //         String messageId = ChatMessageClientContentUtil.getMessageId(chatMessage);
    //         ImConvDTO convDetail = imConvManageService.getConvDetail(convId);
    //         // 设置已读
    //         Long maxMessageId = imMessageManageService.updateAllMessageReadByConvIdAndUserId(convId, fromUserId);
    //         if (Objects.equals(convDetail.getConvType(), ImConvTypeEnum.SINGLE.getId())) {
    //             Long otherUserId = Objects.equals(convDetail.getUser1Id(), fromUserId) ? convDetail.getUser2Id() : convDetail.getUser1Id();
    //             Channel otherChannel = userChannelMap.get(otherUserId);
    //             if (otherChannel != null && otherChannel.isActive()) {
    //                 // 给会话对方也发消息，让其标记为已读
    //                 sendSystemMessage(ChartMessageTypeEnum.CONVERSATION_MESSAGE_READ_MARK, userChannelMap.get(otherUserId), new ConversationMessageReadMarkDTO(null, maxMessageId, convId));
    //             }
    //         }
    //     });
    // }

    /**
     * 删除会话
     * @param ctx
     * @param chatMessage
     */
    // private void handleDeleteConversation(ChannelHandlerContext ctx, ChatMessage<Map<String, Object>> chatMessage) {
    //     threadService.execute(ctx, () -> {
    //         log.info("处理删除会话");
    //         Channel currentChannel = ctx.channel();
    //         Long convId = ChatMessageClientContentUtil.getConvId(chatMessage);
    //         String messageId = ChatMessageClientContentUtil.getMessageId(chatMessage);
    //         Long userId = channelUserMap.get(currentChannel);
    //         imConvManageService.deleteConv(convId, userId);
    //         sendSystemMessage(ChartMessageTypeEnum.DELETE_CONVERSATION, currentChannel, new DeleteConversationDTO(messageId, convId));
    //     });
    // }

    /**
     * 广播消息
     */
    // private void broadcastMessage(ChatMessage message, Channel excludeChannel) {
    //     String jsonMessage = JsonUtil.toJsonString(message);
    //     TextWebSocketFrame frame = new TextWebSocketFrame(jsonMessage);
    //
    //     for (Channel channel : channels) {
    //         if (channel != excludeChannel && channel.isActive()) {
    //             channel.writeAndFlush(frame.retainedDuplicate());
    //         }
    //     }
    // }

    /**
     * 发送系统消息
     */
    // private void sendSystemMessage(Channel channel, String content) {
    //     ChatMessage<String> systemMessage = new ChatMessage<String>(
    //             CommonConst.SYSTEM_USER_ID,
    //             channelUserMap.get(channel),
    //             content,
    //             ChartMessageTypeEnum.SYSTEM
    //     );
    //     channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(systemMessage)));
    // }

    /**
     * 发送系统消息
     */
    // private void sendSystemMessage(Channel channel, IServerContent serverContent) {
    //     ChatMessage<IServerContent> systemMessage = new ChatMessage<IServerContent>(
    //             CommonConst.SYSTEM_USER_ID,
    //             channelUserMap.get(channel),
    //             serverContent,
    //             ChartMessageTypeEnum.SYSTEM
    //     );
    //     channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(systemMessage)));
    // }

    /**
     * 发送系统消息
     * @param channel   接收消息的Channel
     * @param serverContent 服务器回复的内容
     * @param chartMessageTypeEnum 消息类型
     */
    // private void sendSystemMessage(ChartMessageTypeEnum chartMessageTypeEnum, Channel channel, IServerContent serverContent) {
    //     ChatMessage<IServerContent> systemMessage = new ChatMessage<IServerContent>(
    //             CommonConst.SYSTEM_USER_ID,
    //             channelUserMap.get(channel),
    //             serverContent,
    //             chartMessageTypeEnum
    //     );
    //     channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(systemMessage)));
    // }

    /**
     * 发送系统消息
     * @param chartMessageTypeEnum 消息类型
     * @param channel   接收消息的Channel
     * @param content 服务器回复的内容
     */
    // public void sendSystemMessage(ChartMessageTypeEnum chartMessageTypeEnum, Channel channel, String content) {
    //     ChatMessage<String> systemMessage = new ChatMessage<String>(
    //             CommonConst.SYSTEM_USER_ID,
    //             channelUserMap.get(channel),
    //             content,
    //             chartMessageTypeEnum
    //     );
    //     channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(systemMessage)));
    // }

    /**
     * 获取Channel对应的用户ID
     */
    public static Long getUserIdByChannel(Channel channel) {
        return channelUserMap.get(channel);
    }

    /**
     * 获取用户ID对应的Channel
     */
    public static Channel getChannelByUserId(Long userId) {
        return userChannelMap.get(userId);
    }

    /**
     * 检查用户是否在线
     */
    public static boolean isUserOnline(Long userId) {
        Channel channel = userChannelMap.get(userId);
        return channel != null && channel.isActive();
    }

    /**
     * 获取在线用户数量
     */
    public static int getOnlineUserCount() {
        return userChannelMap.size();
    }

    /**
     * 获取所有在线用户ID
     */
    public static Set<Long> getOnlineUserIds() {
        return userChannelMap.keySet();
    }

    /**
     * 强制用户下线
     */
    public static void forceUserOffline(Long userId) {
        Channel channel = userChannelMap.get(userId);
        if (channel != null && channel.isActive()) {
            channel.close();
            log.info("强制用户 {} 下线", userId);
        }
    }

    /**
     * 发送消息给指定用户
     */
    // public static boolean sendMessageToUser(Long userId, ChatMessage message) {
    //     Channel channel = userChannelMap.get(userId);
    //     return sendMessageToUser(channel, message);
    // }

    /**
     * 发送消息给指定用户
     */
    // public static boolean sendMessageToUser(Channel channel, ChatMessage message) {
    //     if (channel != null && channel.isActive()) {
    //         channel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJsonString(message)));
    //         return true;
    //     }
    //     return false;
    // }


    /**
     * 全局异常处理器
     * @param ctx 上下文
     * @param cause 异常
     */
    public static void exceptionCaughtStatic(ChannelHandlerContext ctx, Throwable cause) {
        log.error("=== 全局异常处理器捕获异常 ===");
        log.error("远程地址: {}", ctx.channel().remoteAddress());
        log.error("异常类型: {}", cause.getClass().getSimpleName());
        log.error("异常信息: {}", cause.getMessage());
        log.error("完整堆栈:", cause);
        log.error("Netty处理异常，远程地址: {}", ctx.channel().remoteAddress(), cause);

        // ChatMessage errorMessage = new ChatMessage<>(
        //         ChatMessageErrorEnum.SERVER_ERROR,
        //         CommonConst.SYSTEM_USER_ID,
        //         NettyServerHandler.getUserIdByChannel(ctx.channel()),
        //         cause.getMessage(),
        //         ChartMessageTypeEnum.SYSTEM
        // );
        //
        // // 如果是业务异常，可以返回具体的错误信息
        // if (cause instanceof IllegalArgumentException) {
        //     errorMessage.setErrCode(ChatMessageErrorEnum.CLIENT_PARAM_ERROR.getErrCode());
        //     errorMessage.setErrMsg(ChatMessageErrorEnum.CLIENT_PARAM_ERROR.getErrMsg());
        // }

        // // 发送错误信息给客户端
        // String errorMsg = JsonUtil.toJsonString(errorMessage);
        // ctx.writeAndFlush(new TextWebSocketFrame(errorMsg));
        // 直接关闭连接
        ctx.close();
    }
}
