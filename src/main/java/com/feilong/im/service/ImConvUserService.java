package com.feilong.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feilong.im.entity.ImConvUser;

/**
 * <p>
 * im用户会话表 服务类
 * </p>
 *
 * @author author
 * @since 2026-02-25
 */
public interface ImConvUserService extends IService<ImConvUser> {
    /**
     * 获取或创建会话用户
     * @param convId 会话ID
     * @param userId 用户ID
     * @return 会话用户
     */
    ImConvUser getOrCreate(Long convId, Long userId);
}
