package com.feilong.im.service;


import java.util.List;
import java.util.Map;

/**
 * @author cfl 2026/02/28
 */
public interface ImMessageManageService {

    // /**
    //  * 发送单聊消息
    //  *
    //  * @param convId      会话ID
    //  * @param message     消息
    //  * @return 发送的消息
    //  */
    // ImMessage sendSingleMessage(Long convId, ChatMessage<Map<String, Object>> message);
    //
    // /**
    //  * 修改消息状态
    //  * @param messageId 消息ID
    //  * @param status    消息状态
    //  * @return 修改结果
    //  */
    // boolean updateMessageStatus(Long messageId, ImMessageStatusEnum status);
    //
    // /**
    //  * 修改用户在会话中的消息全部已读
    //  * @param convId 会话ID
    //  * @param userId 用户ID
    //  * @return 标记已读的最大ID
    //  */
    // Long updateAllMessageReadByConvIdAndUserId(Long convId, Long userId);
    //
    // /**
    //  * 获取历史聊天信息
    //  * @param convId 会话ID
    //  * @param minMsgId 最小消息ID
    //  * @param limit 查询条数
    //  * @return 聊天记录列表
    //  */
    // List<ImMessageDTO> listHistoryMessage(Long convId, Long minMsgId, int limit);
    //
    // /**
    //  * 获取未读消息数量，并修改会话的未读数量
    //  * @param convId 会话ID
    //  * @param toUserId 接收用户ID
    //  * @return 未读消息数量
    //  */
    // Integer getUnreadCount(Long convId, Long toUserId);
}
