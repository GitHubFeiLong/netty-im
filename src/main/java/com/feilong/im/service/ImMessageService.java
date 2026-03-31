package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feilong.im.entity.ImMessage;

/**
 * <p>
 * im消息表 服务类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
public interface ImMessageService extends IService<ImMessage> {

    /**
     * 获取未读消息数量
     * @param convId 会话ID
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    int countUnreadMessage(Long convId, Long receiverId);

}
