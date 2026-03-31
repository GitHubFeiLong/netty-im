package com.feilong.im.service;

import com.feilong.im.dto.ImConvDTO;

import java.util.List;

/**
 * 会话相关管理服务
 * @author cfl 2026/02/27
 */
public interface ImConvManageService {

    // /**
    //  * 获取会话详情
    //  * @param convId 会话ID
    //  * @return 会话详情
    //  */
    // ImConvDTO getConvDetail(Long convId);
    //
    /**
     * 创建单聊会话
     * @param minUserId 较小的用户ID
     * @param maxUserId 较大的用户ID
     * @return 会话
     */
    ImConvDTO createSingleConv(Long minUserId, Long maxUserId);
    //
    // /**
    //  * 根据用户1id查询会话列表
    //  * @param user1Id 用户1id
    //  * @return  会话列表
    //  */
    // List<ImConvDTO> listConvListByUserId(Long user1Id);
    //
    // /**
    //  * 删除会话
    //  * @param convId 会话ID
    //  * @param userId 用户ID
    //  */
    // void deleteConv(Long convId, Long userId);
}
