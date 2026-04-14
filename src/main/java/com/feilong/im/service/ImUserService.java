package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feilong.im.dto.ImUserDTO;
import com.feilong.im.dto.req.ImSignUpReq;
import com.feilong.im.entity.ImUser;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>
 * im账户表 服务类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
public interface ImUserService extends IService<ImUser> {
    /**
     * 获取缓存中的用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    ImUserDTO getCatchById(Long userId);

    /**
     * 根据用户ID查询，不存在时进行创建
     * @param userId 用户ID
     * @return IM账户
     */
    ImUser getOrCreateById(Long userId);

    /**
     * 更新用户状态
     * @param id  用户ID
     * @param status  用户状态（0-离线，1-在线，2-忙碌，3-隐身）
     * @return  更新结果
     */
    boolean updateStatus(Long id, int status);

    /**
     * 根据用户ID列表查询用户信息
     * @param userIds  用户ID列表
     * @return  用户信息列表
     */
    List<ImUser> listInfoByUserIds(List<Long> userIds);
}
