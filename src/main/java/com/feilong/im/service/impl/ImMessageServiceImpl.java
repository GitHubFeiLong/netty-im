package com.feilong.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feilong.im.dao.ImMessageMapper;
import com.feilong.im.entity.ImMessage;
import com.feilong.im.enums.status.ImMessageStatusEnum;
import com.feilong.im.service.ImMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * im消息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {

    /**
     * 获取未读消息数量
     *
     * @param convId     会话ID
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    @Override
    public int countUnreadMessage(Long convId, Long receiverId) {
        return super.lambdaQuery()
                .eq(ImMessage::getConvId, convId)
                .eq(ImMessage::getReceiverId, receiverId)
                .between(ImMessage::getStatus, ImMessageStatusEnum.DELIVERED_TO_SERVER.getId(), ImMessageStatusEnum.DELIVERED_TO_RECEIVER.getId())
                .count()
                .intValue();
    }
}
