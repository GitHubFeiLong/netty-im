package com.feilong.im.service.impl;

import com.feilong.im.service.ImMessageManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author cfl 2026/02/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImMessageManageServiceImpl implements ImMessageManageService {
    // private final ImMessageService imMessageService;
    // private final ImConvService imConvService;
    // private final ImConvUserService imConvUserService;
    // private final ImGroupMemberService imGroupMemberService;
    // private final ImMessageEntityMapper imMessageEntityMapper;
    //
    //
    // /**
    //  * 发送单聊消息
    //  *
    //  * @param convId  会话ID
    //  * @param message 消息
    //  * @return 发送的消息
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public ImMessage sendSingleMessage(Long convId, ChatMessage<Map<String, Object>> message) {
    //     Assert.isTrue(convId != null, "convId不能为空");
    //     Long fromUserId = message.getFromUserId();
    //     Long toUserId = message.getToUserId();
    //     Assert.isTrue(fromUserId != null, "fromUserId不能为空");
    //     Assert.isTrue(toUserId != null, "toUserId不能为空");
    //
    //     ImConvDTO imConv = imConvService.getCatchById(convId);
    //     Date now = CurrentTimeContext.get();
    //     if (Objects.equals(ImConvTypeEnum.SINGLE.getId(), imConv.getConvType())) {
    //         ImMessage imMessage = new ImMessage();
    //         imMessage.setConvId(imConv.getId());
    //         imMessage.setSenderId(message.getFromUserId());
    //         imMessage.setReceiverType(imConv.getConvType());
    //         imMessage.setReceiverId(toUserId);
    //         imMessage.setMsgType(ChatMessageClientContentUtil.getMsgType(message));
    //         imMessage.setContent(ChatMessageClientContentUtil.getContent(message));
    //         imMessage.setSendTime(now);
    //         // 先送到服务器
    //         imMessage.setStatus(ImMessageStatusEnum.DELIVERED_TO_SERVER.getId());
    //         imMessage.setExtra(ChatMessageClientContentUtil.getExtra(message));
    //         imMessage.setUpdateTime(now);
    //
    //         imMessageService.save(imMessage);
    //
    //         // 修改会话最后消息ID
    //         imConvService.updateLastMsgId(convId, imMessage.getId());
    //
    //         // 校验对方会话是否存在
    //         imConvUserService.getOrCreate(convId, toUserId);
    //         return imMessage;
    //     }
    //
    //     throw new RuntimeException("会话类型非单聊");
    // }
    //
    // /**
    //  * 修改消息状态
    //  *
    //  * @param messageId 消息ID
    //  * @param status    消息状态
    //  * @return 修改结果
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public boolean updateMessageStatus(Long messageId, ImMessageStatusEnum status) {
    //     return imMessageService.lambdaUpdate().set(ImMessage::getStatus, status.getId()).set(ImMessage::getUpdateTime, CurrentTimeContext.get()).eq(ImMessage::getId, messageId).update();
    // }
    //
    // /**
    //  * 修改用户在会话中的消息全部已读
    //  *
    //  * @param convId 会话ID
    //  * @param userId 用户ID
    //  * @return 标记已读的最大ID
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public Long updateAllMessageReadByConvIdAndUserId(Long convId, Long userId) {
    //     log.debug("修改会话{}，用户{}消息都已读", convId, userId);
    //     // 查询最后一条会话
    //     ImConvDTO imConv = imConvService.getCatchById(convId);
    //
    //     Date now = CurrentTimeContext.get();
    //     imConvUserService.lambdaUpdate()
    //             .set(ImConvUser::getLastReadId, imConv.getLastMsgId())
    //             .set(ImConvUser::getUnreadCount, 0)
    //             .set(ImConvUser::getUpdateTime, now)
    //             .eq(ImConvUser::getConvId, convId)
    //             .eq(ImConvUser::getUserId, userId)
    //             .update();
    //
    //     // 更新所有消息的状态为已读
    //     imMessageService.lambdaUpdate()
    //             .set(ImMessage::getStatus, ImMessageStatusEnum.READ.getId())
    //             .set(ImMessage::getUpdateTime, now)
    //             .eq(ImMessage::getConvId, convId)
    //             .eq(ImMessage::getReceiverId, userId)
    //             .update();
    //     ImMessage maxRedMessage = imMessageService.lambdaQuery()
    //             .eq(ImMessage::getConvId, convId)
    //             .eq(ImMessage::getReceiverId, userId)
    //             .eq(ImMessage::getStatus, ImMessageStatusEnum.READ.getId())
    //             .orderByDesc(ImMessage::getId)
    //             .last("limit 1")
    //             .one();
    //     return maxRedMessage != null ? maxRedMessage.getId() : null;
    // }
    //
    // /**
    //  * 获取历史聊天信息
    //  *
    //  * @param convId   会话ID
    //  * @param minMsgId 最小消息ID
    //  * @param limit    查询条数
    //  * @return 聊天记录列表
    //  */
    // @Override
    // public List<ImMessageDTO> listHistoryMessage(Long convId, Long minMsgId, int limit) {
    //     List<ImMessage> list = imMessageService.lambdaQuery()
    //             .eq(ImMessage::getConvId, convId)
    //             .lt(minMsgId != null, ImMessage::getId, minMsgId)
    //             .orderByDesc(ImMessage::getId)
    //             .last("limit " + limit)
    //             .list();
    //     // 将其升序
    //     list.sort(Comparator.comparing(ImMessage::getId));
    //     return imMessageEntityMapper.toDto(list);
    // }
    //
    // /**
    //  * 获取未读消息数量，并修改会话的未读数量
    //  *
    //  * @param convId   会话ID
    //  * @param userId   用户ID
    //  * @return 未读消息数量
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public Integer getUnreadCount(Long convId, Long userId) {
    //     // 查询会话
    //     ImConvDTO imConv = imConvService.getCatchById(convId);
    //     // 查询会话下用户未读的消息数量
    //     int unreadCount = imMessageService.countUnreadMessage(convId, userId);
    //     // 修改会话未读数量
    //     imConvUserService.lambdaUpdate()
    //             .set(ImConvUser::getUnreadCount, unreadCount)
    //             .set(ImConvUser::getUpdateTime, CurrentTimeContext.get())
    //             .eq(ImConvUser::getConvId, convId)
    //             .eq(ImConvUser::getUserId, userId)
    //             .update();
    //     return unreadCount;
    // }
}
