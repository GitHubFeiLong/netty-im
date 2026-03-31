package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feilong.im.dto.ImConvDTO;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.entity.ImConv;

import java.util.Set;

/**
 * <p>
 * im会话表 服务类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
public interface ImConvService extends IService<ImConv> {

    /**
     * 获取缓存中的用户信息
     * @param minUserId 较小的用户ID
     * @param maxUserId 较大的用户ID
     * @return 用户信息
     */
    ImConvDTO getSingleCatchById(Long minUserId, Long maxUserId);

    // /**
    //  * 根据会话ID查询会话信息
    //  * @param convId 会话ID
    //  * @return 会话信息
    //  */
    // ImConvDTO getCatchById(Long convId);
    //
    // /**
    //  * 更新会话最后一条消息ID
    //  * @param convId    会话ID
    //  * @param lastMsgId 消息ID
    //  * @return 更新结果
    //  */
    // boolean updateLastMsgId(Long convId, Long lastMsgId);
    //
    // /**
    //  * 查询用户相关的指定类型的所有会话ID
    //  * @param userId 用户ID
    //  * @param convType 会话类型
    //  * @return 会话ID集合
    //  */
    // Set<Long> queryConvIdsByUserId(Long userId, ImConvTypeEnum convType);
}
